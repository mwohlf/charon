import {Component, OnInit} from '@angular/core';
import {
  OidcSecurityService,
  PublicEventsService,
} from 'angular-auth-oidc-client';
import {enableHeaderFlag} from './action';
import {Store} from '@ngrx/store';
import {AppState} from '../app-shell.module';
import {authorizeAction, logoffAction} from '../oauth/action';
import {Observable} from 'rxjs';
import {isAuthenticated} from '../oauth/selector';
import {readConfigurationDetailsUsingGET} from '../config/action';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
})
export class HeaderComponent implements OnInit {

  isAuthenticated$: Observable<boolean | undefined>;

  constructor(
    public store: Store<AppState>,
    public oidcSecurityService: OidcSecurityService,
  ) {
    this.isAuthenticated$ = this.store.select(isAuthenticated);
  }

  ngOnInit(): void {
  }

  refresh() {
    this.store.dispatch(readConfigurationDetailsUsingGET());
    this.oidcSecurityService.authorize();
  }

  login() {
   // this.store.dispatch(authorizeAction());
    this.oidcSecurityService.authorize();
  }

  logout() {
    this.oidcSecurityService.logoffAndRevokeTokens();
    // this.store.dispatch(logoffAction());
  }
}


/*

  login() {
    this.oidcSecurityService.authorize();
    this.store.dispatch(enableHeaderFlag({
      payload: {isEnabled: true},
    }));
    // this.oidcSecurityService.authorize();
    console.log('clicked');
  }

  logout() {
    this.oidcSecurityService.logoff();
 */
