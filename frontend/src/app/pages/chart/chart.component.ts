import {Component, OnInit} from '@angular/core';
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
import {FitData} from '../../components/fit-data/fit-data';
import * as d3 from 'd3';

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
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.scss'],
})
export class ChartComponent implements OnInit {

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
    this.createRectangle();
  }

  // see: https://d3-graph-gallery.com/line.html
  createRectangle() {
    let svg = d3.select('svg#chart1');

    svg.append('rect')
      .attr('width', '250')
      .attr('height', '100')
      .attr('x', '200')
      .attr('y', '100')
      .attr('fill', 'violet');

    var dataset1 = [
      [1, 1], [12, 20], [24, 36],
      [32, 50], [40, 70], [50, 100],
      [55, 106], [65, 123], [73, 130],
      [78, 134], [83, 136], [89, 138],
      [100, 140],
    ];

    // TODO:
    //  - fix backend to return a timeseries
    //  - scale and render there

    var xScale = d3.scaleLinear<number>().domain([0, 100]).range([0, 700]);
    var yScale = d3.scaleLinear<number>().domain([0, 200]).range([500, 0]);

    // https://www.educative.io/answers/how-to-create-a-line-chart-using-d3
    // https://stackoverflow.com/questions/42308115/d3v4-typescript-angular2-error-using-d3-line-xd-function
    var line: d3.Line<number[]> = d3.line<number[]>()
      .x(function(d: number[]) { return xScale(d[0]); })
      .y(function(d: number[]) { return yScale(d[1]); })
      .curve(d3.curveMonotoneX);

    svg.append("path")
      .datum(dataset1)
      .attr("class", "line")
      .attr("transform", "translate(" + 100 + "," + 100 + ")")
      .attr("d", line)
      .style("fill", "none")
      .style("stroke", "#CC0000")
      .style("stroke-width", "2");

  }

}
