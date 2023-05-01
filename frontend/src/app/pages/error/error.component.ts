import {Component, OnInit} from '@angular/core';
import {PageConfig} from '../page-config';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.scss'],
})
export class ErrorComponent implements OnInit {

  static SPEC: PageConfig = {
    route: 'error',
    icon: 'error',
    title: 'Error',
    requiredRoles: [],
    component: ErrorComponent,
  };

  constructor() {
  }

  ngOnInit(): void {
  }

}
