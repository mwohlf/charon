import {Component, HostBinding, OnInit} from '@angular/core';
import {PageConfig} from '../page-config';
import {PageComponent} from '../../components/page/page.component';
import {MatCardModule} from '@angular/material/card';
import {AsyncPipe, NgIf} from '@angular/common';
import {Store} from '@ngrx/store';
import {AppState} from '../../app-shell.module';
import {
  selectDataFeature,
  selectProtectedData,
} from '../../modules/data/selector';
import {Observable} from 'rxjs';
import {ProtectedData} from 'build/generated';
import {readProtectedDataUsingGET} from '../../modules/data/action';
import {NGXLogger} from 'ngx-logger';
import {
  FitSourcesGrid,
} from '../../components/fit-sources-grid/fit-sources-grid';
import {FitDataChart} from '../../components/fit-data-chart/fit-data-chart';
import {DataState} from '../../modules/data/reducer';

@Component({
  imports: [
    PageComponent,
    MatCardModule,
    NgIf,
    AsyncPipe,
    FitSourcesGrid,
    FitDataChart,
  ],
  standalone: true,
  selector: 'app-protected',
  templateUrl: './protected.component.html',
})
export class ProtectedComponent implements OnInit {

  @HostBinding('class') class = 'main-content';

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

  data$: Observable<DataState>;

  constructor(
    private store: Store<AppState>,
    private logger: NGXLogger,
  ) {
    this.data$ = this.store.select(selectDataFeature);
  }

  ngOnInit(): void {
    this.logger.info('<ProtectedComponent> trigger dispatch to get data');
    this.store.dispatch(readProtectedDataUsingGET());
  }

}
