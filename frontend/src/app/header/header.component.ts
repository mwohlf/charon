import {Component, OnInit} from '@angular/core';
import {
  OidcSecurityService,
  PublicEventsService,
} from 'angular-auth-oidc-client';
import {enableHeaderFlag} from './action';
import {Store} from '@ngrx/store';
import {AppState} from '../app-shell.module';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
})
export class HeaderComponent implements OnInit {

  constructor(
    private store: Store<AppState>,
    private oidcSecurityService: OidcSecurityService,
    private eventService: PublicEventsService,
  ) {
    //
  }

  ngOnInit(): void {
  }

  login() {
    this.store.dispatch(enableHeaderFlag({
      payload: {isEnabled: true},
    }));
    // this.oidcSecurityService.authorize();
    console.log('clicked');
  }

  logout() {
    this.oidcSecurityService.logoff();
  }
}
