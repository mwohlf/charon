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

import {selectFitnessDataItem} from '../../modules/fitness/selector';
import {Observable} from 'rxjs';

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
  ],
  selector: 'fit-data',
  standalone: true,
  templateUrl: './fit-data.html',


})
export class FitData implements OnInit {

   selectFitnessDataItem$: Observable<FitnessDataItem | undefined>;

  constructor(
    private store: Store<AppState>,
    private logger: NGXLogger,
  ) {
    this.selectFitnessDataItem$ = this.store.select(selectFitnessDataItem);
  }

  ngOnInit(): void {
    this.logger.info('<FitData> ngOnInit called');
  }


}
