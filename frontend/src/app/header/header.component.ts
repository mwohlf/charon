import {Component, OnInit} from '@angular/core';
import {
  OidcSecurityService,
} from 'angular-auth-oidc-client';
import {Store} from '@ngrx/store';
import {AppState} from '../app-shell.module';
import {loginAction, logoutAction} from '../oauth/action';
import {Observable} from 'rxjs';
import {isAuthenticated} from '../oauth/selector';
import {readConfigurationDetailsUsingGET} from '../config/action';
import {SIMPLE_CONFIG} from '../oauth/reducer';

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
    this.store.dispatch(loginAction({payload: {configId: SIMPLE_CONFIG}}));
  }

  logout() {
    this.store.dispatch(logoutAction());
  }
}
