import {Component, HostBinding, OnInit} from '@angular/core';
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
  selector: 'app-main',
  templateUrl: './admin.component.html',
})
export class AdminComponent implements OnInit {

  @HostBinding('class') class = 'main-content';

  static SPEC: PageConfig = {
    route: 'admin',
    icon: 'stars',
    title: 'Admin',
    requiredRoles: ['admin'],
    component: AdminComponent,
    inMenu: () => {return true;},
  };

  constructor() {
  }

  ngOnInit(): void {
  }

}
