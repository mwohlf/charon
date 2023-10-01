import {Component, HostBinding, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from '../../app-shell.module';
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
  selector: 'app-login',
  templateUrl: './login.component.html',
})
export class LogoutComponent implements OnInit {

  @HostBinding('class') class = 'main-content';

  static SPEC: PageConfig = {
    route: 'logout',
    icon: 'logout',
    title: 'Logout',
    requiredRoles: [],
    component: LogoutComponent,
    inMenu: () => {return true;},
  };

  constructor(
    private store: Store<AppState>,
  ) {
  }

  ngOnInit(): void {
  }

}
