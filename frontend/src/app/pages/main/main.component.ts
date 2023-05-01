import {Component, OnInit} from '@angular/core';
import {PageConfig} from '../page-config';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
})
export class MainComponent implements OnInit {

  static SPEC: PageConfig = {
    route: 'main',
    icon: 'stars',
    title: 'Main',
    requiredRoles: [],
    component: MainComponent,
  }

  constructor() {
  }

  ngOnInit(): void {
  }

}
