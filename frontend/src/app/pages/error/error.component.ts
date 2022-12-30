import {Component, OnInit} from '@angular/core';
import {page_spec} from '../page.spec';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.scss'],
})
export class ErrorComponent implements OnInit {

  static SPEC: page_spec = {
    route: 'error',
    icon: 'error',
    title: 'Error',
    component: ErrorComponent,
  }

  constructor() {
  }

  ngOnInit(): void {
  }

}
