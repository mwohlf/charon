import {Component, Input, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from '../../app-shell.module';
import {NGXLogger} from 'ngx-logger';
import {selectRandomData} from '../../modules/data/selector';
import {selectDataSources} from '../../modules/fit/selector';
import {Observable} from 'rxjs';
import { FitDataSource } from 'build/generated';
import {AsyncPipe, NgIf} from '@angular/common';
import {MatCardModule} from '@angular/material/card';
import {readRandomDataUsingGET} from '../../modules/data/action';
import {readFitDataSourcesUsingGET} from '../../modules/fit/action';

@Component({
  imports: [
    AsyncPipe,
    MatCardModule,
    NgIf,
  ],
  selector: 'fit-sources-grid',
  standalone: true,
  templateUrl: './fit-sources-grid.html',
})
export class FitSourcesGrid implements OnInit {

  dataSources$: Observable<Array<FitDataSource> | undefined>;



  constructor(
    private store: Store<AppState>,
    private logger: NGXLogger,
  ) {
    this.dataSources$ = this.store.select(selectDataSources);
  }

  ngOnInit(): void {
    this.logger.info('<FitSourcesGrid> trigger dispatch to get data');
    this.store.dispatch(readFitDataSourcesUsingGET());
  }

}
