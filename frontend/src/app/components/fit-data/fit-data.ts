import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from '../../app-shell.module';
import {NGXLogger} from 'ngx-logger';
import {AsyncPipe, NgForOf, NgIf} from '@angular/common';
import {MatCardModule} from '@angular/material/card';
import {MatTableModule} from '@angular/material/table';
import {FitnessDataItem} from 'build/generated';
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

import {selectFitnessDataItem} from '../../modules/fitness/selector';
import {Observable} from 'rxjs';
import {FitnessState} from '../../modules/fitness/reducer';
import {
  setFitnessTimeSeriesBegin,
  setFitnessTimeSeriesEnd,
} from '../../modules/fitness/action';

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
  ],
  selector: 'fit-data',
  standalone: true,
  templateUrl: './fit-data.html',


})
export class FitData implements OnInit {

  selectFitnessDataItem$: Observable<FitnessDataItem | undefined>;

  constructor(
    private store: Store<FitnessState>,
    private logger: NGXLogger,
  ) {
    this.selectFitnessDataItem$ = this.store.select(selectFitnessDataItem);
  }

  ngOnInit(): void {
    this.logger.info('<FitData> ngOnInit called');
  }

  beginChanged($event: MatDatepickerInputEvent<ExtractDateTypeFromSelection<DateRange<Date>>, DateRange<Date>>) {
    this.logger.info('<beginChanged> value: ' + JSON.stringify($event.value, null, 2));
    this.store.dispatch(setFitnessTimeSeriesEnd({
        payload: {
          endInMillisecond: undefined,
        },
      },
    ));
    this.store.dispatch(setFitnessTimeSeriesBegin({
        payload: {
          beginInMillisecond: $event.value?.getTime(),
        },
      },
    ));
  }

  endChanged($event: MatDatepickerInputEvent<ExtractDateTypeFromSelection<DateRange<Date>>, DateRange<Date>>) {
    this.logger.info('<endChanged> value: ' + JSON.stringify($event.value, null, 2));
    this.store.dispatch(setFitnessTimeSeriesEnd({
        payload: {
          endInMillisecond: $event.value?.getTime(),
        },
      },
    ));
  }

}
