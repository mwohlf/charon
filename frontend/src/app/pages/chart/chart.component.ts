import {Component, HostBinding, OnInit} from '@angular/core';
import {PageConfig} from '../page-config';
import {PageComponent} from '../../components/page/page.component';
import {MatCardModule} from '@angular/material/card';
import {AsyncPipe, NgIf} from '@angular/common';
import {Store} from '@ngrx/store';
import {AppState} from '../../app-shell.module';
import {NGXLogger} from 'ngx-logger';
import {
  FitSourcesGrid,
} from '../../components/fit-sources-grid/fit-sources-grid';
import {FitDataChart} from '../../components/fit-data-chart/fit-data-chart';
import * as d3 from 'd3';

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
  selector: 'app-chart',
  templateUrl: './chart.component.html',
})
export class ChartComponent implements OnInit {

  @HostBinding('class') class = 'main-content';

  static SPEC: PageConfig = {
    route: 'chart',
    icon: 'loop',
    title: 'Chart',
    requiredRoles: [],
    component: ChartComponent,
    inMenu: () => {
      return true;
    },
  };

  constructor(
    private store: Store<AppState>,
    private logger: NGXLogger,
  ) {
  }

  ngOnInit(): void {
    this.logger.info('<ChartComponent> ');
  }

}
