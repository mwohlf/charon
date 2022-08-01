import {Injectable} from '@angular/core';
import {NGXLogger} from 'ngx-logger';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {
  LoginResponse,
  OidcClientNotification,
  OidcSecurityService,
  PublicEventsService,
} from 'angular-auth-oidc-client';
import {Observable} from 'rxjs';
import {
  loginAction,
  logoutAction,
  oauthEventAction,
  oidcSecurityAction,
} from './action';
import {Action, Store} from '@ngrx/store';
import {tap} from 'rxjs/operators';
import {AppState} from '../app-shell.module';

@Injectable()
export class Effects {

  constructor(
    private store: Store<AppState>,
    private oidcSecurityService: OidcSecurityService,
    private eventService: PublicEventsService,
    private logger: NGXLogger,
    private action$: Actions,
  ) {

    // this reads the callback url params from oauth
    // apparently we are only supposed to call this once on initial load,
    // or on auth redirect, then the magic happens and the oauth lib state machine
    // is doing its thing
    this.oidcSecurityService.checkAuth().subscribe((next: LoginResponse) => {
      console.log('checkAuth ', next);
      this.store.dispatch(oidcSecurityAction({payload: next}));
    });

    this.eventService
      .registerForEvents()
      .subscribe((next: OidcClientNotification<any>) => {
        this.store.dispatch(oauthEventAction({payload: next}));
      });
  }

  loginAction$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(loginAction),
      tap(next => {
        console.log('authorizeAction');
        // see: https://nice-hill-002425310.azurestaticapps.net/docs/documentation/public-api
        this.oidcSecurityService.authorize(next.payload.configId); // this performs a browser redirect to the login page
      }),
    );
  }, {dispatch: false});

  logoutAction$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(logoutAction),
      tap(() => {
        // see: https://nice-hill-002425310.azurestaticapps.net/docs/documentation/login-logout
        console.log('logoffAction...');
        // this.oidcSecurityService.logoff();
        // this.oidcSecurityService.logoffLocal();
        this.oidcSecurityService.logoffAndRevokeTokens()
          .subscribe((result) => console.log(result));
      }),
    );
  }, {dispatch: false});

}
