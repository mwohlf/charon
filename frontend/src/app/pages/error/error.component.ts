import {Component, OnInit} from '@angular/core';
import {PageConfig} from '../page-config';
import {PageComponent} from '../../components/page/page.component';
import {MatCardModule} from '@angular/material/card';
import {AsyncPipe, NgIf} from '@angular/common';

@Component({
  imports: [
    PageComponent,
    MatCardModule,
    NgIf,
    AsyncPipe,
  ],
  standalone: true,
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
