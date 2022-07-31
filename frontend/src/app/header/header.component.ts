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
import {SIMPLE_CONFIG} from '../oauth/oauth.module';

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

  reReadConfig() {
    this.store.dispatch(readConfigurationDetailsUsingGET());
  }

  authorize() {
    this.oidcSecurityService.authorize();
  }

  logout() {
    console.error("logout...");
    this.oidcSecurityService.revokeAccessToken()
      .subscribe((result) => console.log(result));

    //
    // this.oidcSecurityService.logoffAndRevokeTokens(SIMPLE_CONFIG).subscribe( next => {
    //  console.log("logout next:", next);
    // })
    //
    // only local cleanup, the session cookie still persists and will be used
    // this.oidcSecurityService.logoff(SIMPLE_CONFIG);
  }
}
