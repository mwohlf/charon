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
import {
  BehaviorSubject,
  combineLatest,
  Observable,
  Subscription,
  timer,
} from 'rxjs';

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
import {debounce, filter, tap} from 'rxjs/operators';
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
  selector: 'fit-data-chart',
  standalone: true,
  templateUrl: './fit-data-chart.html',
  styleUrls: ['./fit-data-chart.scss'],
})
export class FitDataChart implements OnInit, OnDestroy {

  selectFitnessDataItem$: Observable<FitnessDataItem | undefined>;
  selectFitnessDataTimeseries$: Observable<FitnessDataTimeseries | undefined>;
  rectReadOnlySubject: BehaviorSubject<DOMRectReadOnly | undefined> = new BehaviorSubject<DOMRectReadOnly | undefined>(undefined);
  rectReadOnly$: Observable<DOMRectReadOnly | undefined> = this.rectReadOnlySubject.asObservable();

  private chartLine: any | undefined = undefined;
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
      debounce(() => timer(500)),
      tap(([timeseries, rect]) => {

        if (timeseries != undefined && rect != undefined) {
          this.renderChart(rect, timeseries);
        }
      }),
    ).subscribe();
  }

  ngOnInit(): void {
    this.logger.info('<FitDataChart> ngOnInit called');
    
  }

  ngOnDestroy() {
    this.timeseriesSubscription.unsubscribe();
  }

  clearChart() {
    let svg: Selection<d3.BaseType, unknown, HTMLElement, any> = d3.select('svg#chart2');
    svg.selectAll('*').remove();
  }

  renderChart(rect: DOMRectReadOnly, timeseries: FitnessDataTimeseries) {

    const xScale = d3.scaleLinear<number>()
      .domain([timeseries.beginSec, timeseries.endSec])
      .range([0, rect.width]);

    const yScale = d3.scaleLinear<number>()
      .domain([timeseries.minValue, timeseries.maxValue])
      .range([rect.height, 0]);

    this.logger.info('<renderChart> timeseries: ', timeseries);

    // https://www.educative.io/answers/how-to-create-a-line-chart-using-d3
    // https://stackoverflow.com/questions/42308115/d3v4-typescript-angular2-error-using-d3-line-xd-function
    const line: d3.Line<TimeseriesDataPoint> = d3.line<TimeseriesDataPoint>()
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
    this.chartLine = svg.append('path');
    this.logger.info('<appending to> svg: ', svg);

    this.chartLine.datum(timeseries.dataPoints)
      .attr('class', 'line')
      .attr('transform', 'translate(' + 5 + ',' + 5 + ')')
      .attr('d', line)
      .style('fill', 'none')
      .style('stroke', '#CC0000')
      .style('stroke-width', '2');

    const markerLine = svg
      .append('line')
      .attr('x1', 0)
      .attr('x2', 0)
      .attr('y1', 0)
      .attr('y2', rect.height)
      .attr('stroke-width', 3)
      .attr('stroke', 'darkviolet')
      .attr('opacity', 0)

    svg.on('mousemove', (e): void => {
      const pointerCoords: [number, number] = d3.pointer(e)
      const [posX, _ ] = pointerCoords

      markerLine
        .attr('x1', posX)
        .attr('x2', posX)
        .attr('opacity', 1)
    })

    const xAxis = d3.axisBottom(xScale);
    const yAxis = d3.axisLeft(yScale);

    svg.append('g')
      .call(xAxis)
      .call(yAxis);

  } // end render chart

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
