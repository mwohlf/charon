import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType, ROOT_EFFECTS_INIT} from '@ngrx/effects';
import {
  LoginResponse,
  OidcClientNotification,
  OidcSecurityService,
  PublicEventsService,
} from 'angular-auth-oidc-client';
import {Observable, of} from 'rxjs';
import {
  loginAction,
  logoutAction,
  oauthEventAction,
  oidcSecurityAction,
  readClientConfigurationListUsingGET,
  readClientConfigurationListUsingGET_failure,
  readClientConfigurationListUsingGET_success,
} from './action';
import {Action, Store} from '@ngrx/store';
import {catchError, map, mergeMap, tap} from 'rxjs/operators';
import {AppState} from '../app-shell.module';
import {
  ClientConfiguration,
  ConfigurationDetailsService,
} from 'build/generated';

import {ErrorDetails, showError} from '../error/action';

@Injectable()
export class Effects {

  constructor(
    private configurationDetailsService: ConfigurationDetailsService,
    private oidcSecurityService: OidcSecurityService,
    private eventService: PublicEventsService,
    private store: Store<AppState>,
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

  ROOT_EFFECTS_INIT: Observable<Action> = createEffect(() => {
    console.error('register root2');
    return this.action$.pipe(
      ofType(ROOT_EFFECTS_INIT), // the trigger to start loading config
      tap(() => {
        console.error('root effect2');
      }),
      mergeMap(() => {
        return [
          readClientConfigurationListUsingGET(),
        ];
      }),
    );
  });

  // the config loading action
  readClientConfigurationListUsingGET$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readClientConfigurationListUsingGET),
      mergeMap(() => {
        console.log('readClientConfigurationListUsingGET');
        return this.configurationDetailsService.readClientConfigurationList().pipe(
          map((clientConfigurationList: Array<ClientConfiguration>) => {
            return readClientConfigurationListUsingGET_success({
              payload: clientConfigurationList,
            });
          }),
          catchError((error: any) => {
            return of(readClientConfigurationListUsingGET_failure({
              payload: {
                title: 'Config data missing',
                message: 'Config data can\'t be loaded.',
                details: JSON.stringify(error, null, 2),
              },
            }));
          }),
        );
      }),
    );
  });

  // config is ready and loaded
  readClientConfigurationListUsingGET_success$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readClientConfigurationListUsingGET_success),
      tap(action => {
        console.log('readConfigurationDetailsUsingGET_success');
        let clientConfigurationList: Array<ClientConfiguration> = action.payload;
        console.log('clientConfigurationList Loaded', clientConfigurationList);
      }),
    );
  }, {dispatch: false});

  // forward as error action...
  readClientConfigurationListUsingGET_failure$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readClientConfigurationListUsingGET_failure),
      map((action: { payload: ErrorDetails }) => {
        console.log('readConfigurationDetailsUsingGET_failure');
        return showError({payload: action.payload});
      }),
    );
  });

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
