import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from '../../app-shell.module';
import {PageConfig} from '../page-config';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: [],
})
export class LoginComponent implements OnInit {

  static SPEC: PageConfig = {
    route: 'login',
    icon: 'login',
    title: 'login',
    requiredRoles: [],
    component: LoginComponent,
  };

  constructor(
    private store: Store<AppState>,
  ) {
  }

  ngOnInit(): void {
  }

}
