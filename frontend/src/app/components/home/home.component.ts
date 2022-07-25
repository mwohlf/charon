import {Component, OnInit} from '@angular/core';
import { ConfigService } from 'build/generated/api/api';
import { ConfigurationDetails } from 'build/generated/model/models';
import {Observable} from "rxjs";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  static ROUTER_PATH: string = "home";

  buildProperties$: Observable<ConfigurationDetails>;

  constructor(
    private configService: ConfigService
  ) {
    this.buildProperties$ = this.configService.readConfigurationDetails();
  }

  ngOnInit(): void {
  }

}
