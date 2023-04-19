import {Component, OnInit} from '@angular/core';
import {ConfigurationDetails} from 'build/generated/model/models';
import {Observable} from 'rxjs';
import {Store} from '@ngrx/store';
import {AppState} from '../../app-shell.module';
import {
  selectConfigurationDetails,
} from '../../modules/config/selector';
import {page_spec} from '../page.spec';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: [],
})
export class LoginComponent implements OnInit {

  static SPEC: page_spec = {
    route: 'login',
    icon: 'login',
    title: 'login',
    component: LoginComponent,
  }

  constructor(
    private store: Store<AppState>,
  ) {
  }

  ngOnInit(): void {
  }

}
