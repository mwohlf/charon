import {Component, OnInit} from '@angular/core';
import {PageConfig} from '../page-config';
import {PageComponent} from '../../components/page/page.component';
import {MatCardModule} from '@angular/material/card';
import {AsyncPipe, NgIf} from '@angular/common';
import {Store} from '@ngrx/store';
import {AppState} from '../../app-shell.module';
import {selectRandomData} from '../../modules/data/selector';
import {Observable} from 'rxjs';
import {RandomData} from 'build/generated';
import {readRandomDataUsingGET} from '../../modules/data/action';
import {NGXLogger} from 'ngx-logger';
import {
  FitSourcesGrid,
} from '../../components/fit-sources-grid/fit-sources-grid';
import {FitData} from '../../components/fit-data/fit-data';

@Component({
  imports: [
    PageComponent,
    MatCardModule,
    NgIf,
    AsyncPipe,
    FitSourcesGrid,
    FitData,
  ],
  standalone: true,
  selector: 'app-protected',
  templateUrl: './protected.component.html',
  styleUrls: ['./protected.component.scss'],
})
export class ProtectedComponent implements OnInit {

  static SPEC: PageConfig = {
    route: 'protected',
    icon: 'loop',
    title: 'Protected',
    requiredRoles: [],
    component: ProtectedComponent,
    inMenu: () => {
      return true;
    },
  };

  randomData$: Observable<RandomData | undefined>;

  constructor(
    private store: Store<AppState>,
    private logger: NGXLogger,
  ) {
    this.randomData$ = this.store.select(selectRandomData);
  }

  ngOnInit(): void {
    this.logger.info('<ProtectedComponent> trigger dispatch to get data');
    this.store.dispatch(readRandomDataUsingGET());
  }

}
