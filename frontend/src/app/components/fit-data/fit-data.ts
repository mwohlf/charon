import {Component, OnDestroy, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {NGXLogger} from 'ngx-logger';
import {AsyncPipe, NgForOf, NgIf} from '@angular/common';
import {MatCardModule} from '@angular/material/card';
import {MatTableModule} from '@angular/material/table';
import {
  FitnessDataItem,
  FitnessDataTimeseries,
  TimeseriesDataPoint,
} from 'build/generated';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatTooltipModule} from '@angular/material/tooltip';
import {
  DateRange,
  ExtractDateTypeFromSelection,
  MatDatepickerInputEvent,
  MatDatepickerModule,
} from '@angular/material/datepicker';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatNativeDateModule} from '@angular/material/core';
import {BehaviorSubject, combineLatest, Observable, Subscription} from 'rxjs';

import {
  selectFitnessDataItem,
  selectFitnessDataTimeseries,
} from '../../modules/fitness/selector';
import {FitnessState} from '../../modules/fitness/reducer';
import {
  setFitnessTimeseriesBegin,
  setFitnessTimeseriesEnd,
} from '../../modules/fitness/action';
import * as d3 from 'd3';
import {ScaleLinear} from 'd3';
import {filter, tap} from 'rxjs/operators';
import {Selection} from 'd3-selection';
import {AngularResizeEventModule, ResizedEvent} from 'angular-resize-event';

@Component({
  imports: [
    AsyncPipe,
    MatCardModule,
    NgIf,
    NgForOf,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    AngularResizeEventModule,
  ],
  selector: 'fit-data',
  standalone: true,
  templateUrl: './fit-data.html',
  styleUrls: ['./fit-data.scss'],
})
export class FitData implements OnInit, OnDestroy {

  selectFitnessDataItem$: Observable<FitnessDataItem | undefined>;
  selectFitnessDataTimeseries$: Observable<FitnessDataTimeseries | undefined>;
  rectReadOnlySubject: BehaviorSubject<DOMRectReadOnly | undefined> = new BehaviorSubject<DOMRectReadOnly | undefined>(undefined);
  rectReadOnly$: Observable<DOMRectReadOnly | undefined> = this.rectReadOnlySubject.asObservable();

  private path: any | undefined = undefined;
  private timeseriesSubscription: Subscription;

  constructor(
    private store: Store<FitnessState>,
    private logger: NGXLogger,
  ) {
    this.selectFitnessDataItem$ = this.store.select(selectFitnessDataItem);
    this.selectFitnessDataTimeseries$ = this.store.select(selectFitnessDataTimeseries);

    this.timeseriesSubscription = combineLatest([this.selectFitnessDataTimeseries$, this.rectReadOnly$]).pipe(
      // this.timeseriesSubscription = this.selectFitnessDataTimeseries$.pipe(
      filter(([timeseries, rect]) => {
        this.logger.info('########### <filter> ', timeseries);
        return timeseries != undefined && rect != undefined;
      }),
      // debounce(() => timer(1000)),
      tap(([timeseries, rect]) => {

        if (timeseries != undefined && rect != undefined) {
          this.logger.info('<timeseries.beginSec>', timeseries.beginSec);
          this.logger.info('<timeseries.endSec>', timeseries.endSec);

          const xScale = d3.scaleLinear<number>()
            .domain([timeseries.beginSec, timeseries.endSec])
            .range([0, rect.width]);
          const yScale = d3.scaleLinear<number>()
            .domain([timeseries.minValue, timeseries.maxValue])
            .range([rect.height, 0]);
          this.renderChart(xScale, yScale, timeseries?.dataPoints);
        }
      }),
    ).subscribe();
  }

  ngOnInit(): void {
    this.logger.info('<FitData> ngOnInit called');

  }

  ngOnDestroy() {
    this.timeseriesSubscription.unsubscribe();
  }

  clearChart() {
    let svg: Selection<d3.BaseType, unknown, HTMLElement, any> = d3.select('svg#chart2');
    svg.selectAll('*').remove();
  }

  renderChart(
    xScale: ScaleLinear<number, number, never>,
    yScale: ScaleLinear<number, number, never>,
    timeseries: Array<TimeseriesDataPoint>) {


    this.logger.info('<renderChart> timeseries: ', timeseries);

    // https://www.educative.io/answers/how-to-create-a-line-chart-using-d3
    // https://stackoverflow.com/questions/42308115/d3v4-typescript-angular2-error-using-d3-line-xd-function
    var line: d3.Line<TimeseriesDataPoint> = d3.line<TimeseriesDataPoint>()
      .x(function(p: TimeseriesDataPoint) {
        return xScale(p.time);
      })
      .y(function(p: TimeseriesDataPoint) {
        return yScale(p.value);
      })
      .curve(d3.curveMonotoneX);


    //if (this.path != undefined) {
    //  this.path.parentNode.removeChild(this.path);
    //}
    let svg: Selection<d3.BaseType, unknown, HTMLElement, any> = d3.select('svg#chart2');
    this.path = svg.append('path');
    this.logger.info('<appending to> svg: ', svg);

    this.path.datum(timeseries)
      .attr('class', 'line')
      .attr('transform', 'translate(' + 5 + ',' + 5 + ')')
      .attr('d', line)
      .style('fill', 'none')
      .style('stroke', '#CC0000')
      .style('stroke-width', '2');
  }

  beginChanged($event: MatDatepickerInputEvent<ExtractDateTypeFromSelection<DateRange<Date>>, DateRange<Date>>) {
    this.logger.info('<beginChanged> value: ' + JSON.stringify($event.value, null, 2));
    this.clearChart();
    this.store.dispatch(setFitnessTimeseriesEnd({
        payload: {
          endInMillisecond: undefined,
        },
      },
    ));
    this.store.dispatch(setFitnessTimeseriesBegin({
        payload: {
          beginInMillisecond: $event.value?.getTime(),
        },
      },
    ));
  }

  endChanged($event: MatDatepickerInputEvent<ExtractDateTypeFromSelection<DateRange<Date>>, DateRange<Date>>) {
    this.logger.info('<endChanged> value: ' + JSON.stringify($event.value, null, 2));
    this.store.dispatch(setFitnessTimeseriesEnd({
        payload: {
          endInMillisecond: $event.value?.getTime(),
        },
      },
    ));
  }

  onResized($event: ResizedEvent) {
    this.clearChart();
    this.logger.info('<onResized>');
    this.rectReadOnlySubject.next($event.newRect);
  }
}
