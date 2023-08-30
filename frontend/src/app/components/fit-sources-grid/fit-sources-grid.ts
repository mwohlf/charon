import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from '../../app-shell.module';
import {NGXLogger} from 'ngx-logger';
import {AsyncPipe, NgForOf, NgIf} from '@angular/common';
import {MatCardModule} from '@angular/material/card';
import {MatTableModule} from '@angular/material/table';
import {FitSourcesDataSource} from './fit-sources-ds';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatTooltipModule} from '@angular/material/tooltip';
import {
  readFitnessDataItemUsingGET,
  readFitnessDataListUsingGET,
} from '../../modules/fit/action';

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
  selector: 'fit-sources-grid',
  standalone: true,
  templateUrl: './fit-sources-grid.html',
  styleUrls: [
    './_fit-sources-grid.scss',
  ],
  providers: [
    FitSourcesDataSource,
  ],
})
export class FitSourcesGrid implements OnInit {


  displayedColumns = ['name', 'type', 'dataTypeName', 'action'];

  constructor(
    public fitSourcesDataSource: FitSourcesDataSource,
    private store: Store<AppState>,
    private logger: NGXLogger,
  ) {
  }

  ngOnInit(): void {
    this.logger.info('<FitSourcesGrid> trigger dispatch to get data');
    this.store.dispatch(readFitnessDataListUsingGET());
  }

  viewDetails(id: string) {
    this.logger.info('<viewDetails> id: ' + id);
    this.store.dispatch(readFitnessDataItemUsingGET({payload: id}));
  }

}
