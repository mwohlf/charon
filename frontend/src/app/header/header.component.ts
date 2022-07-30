import {Component, OnInit} from '@angular/core';
import {
  OidcSecurityService,
  PublicEventsService,
} from 'angular-auth-oidc-client';
import {enableHeaderFlag} from './action';
import {Store} from '@ngrx/store';
import {AppState} from '../app-shell.module';
import {loginAction, logoutAction} from '../oauth/action';
import {Observable} from 'rxjs';
import {isAuthenticated} from '../oauth/selector';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
})
export class HeaderComponent implements OnInit {

  isAuthenticated$: Observable<boolean | undefined>;

  constructor(
    private store: Store<AppState>,
    private oidcSecurityService: OidcSecurityService,
    private eventService: PublicEventsService,
  ) {
    this.isAuthenticated$ = this.store.select(isAuthenticated);
  }

  ngOnInit(): void {
  }

  login() {
    this.store.dispatch(loginAction({
      payload: {isAuthenticated: true},
    }));
  }

  logout() {
    this.store.dispatch(logoutAction({
      payload: {isAuthenticated: false},
    }));
  }
}
