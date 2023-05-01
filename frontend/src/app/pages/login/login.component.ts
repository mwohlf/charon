import {Component, OnInit} from '@angular/core';
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
