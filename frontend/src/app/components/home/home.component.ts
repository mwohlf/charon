import {Component, OnInit} from '@angular/core';
import { BuildService } from 'build/generated/api/api';
import { BuildProperties } from 'build/generated/model/models';
import {Observable} from "rxjs";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  static ROUTER_PATH: string = "home";

  buildProperties$: Observable<BuildProperties>;

  constructor(
    private buildService: BuildService
  ) {
    this.buildProperties$ = this.buildService.readBuildProperties();
  }

  ngOnInit(): void {
    this.buildService.readBuildProperties()
  }

}
