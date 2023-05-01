import {Component, OnInit} from '@angular/core';
import {PageConfig} from '../page-config';

@Component({
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
  }

  constructor() {
  }

  ngOnInit(): void {
  }

}
