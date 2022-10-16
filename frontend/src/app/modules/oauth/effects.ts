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
import {AppState} from '../../app-shell.module';

import {showNotification} from '../notification/action';
import {
  ClientConfiguration,
  ConfigurationDetailsService,
} from 'build/generated';
import {LocationStrategy} from '@angular/common';

@Injectable()
export class Effects {

  constructor(
    private locationStrategy: LocationStrategy,
    private configurationDetailsService: ConfigurationDetailsService,
    private oidcSecurityService: OidcSecurityService,
    private eventService: PublicEventsService,
    private store: Store<AppState>,
    private action$: Actions,
  ) {

    // this reads the callback url params from oauth,
    // apparently we are only supposed to call this once on initial load,
    // or on returning from the auth redirect,
    // then the magic happens and the oauth lib's state machine is doing its thing
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
    return this.action$.pipe(
      ofType(ROOT_EFFECTS_INIT), // the trigger to start loading config
      tap((action) => {
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
      mergeMap((action) => {
        console.log('readClientConfigurationListUsingGET');
        return this.configurationDetailsService.readClientConfigurationList().pipe(
          map((clientConfigurationList: Array<ClientConfiguration>) => {
            return readClientConfigurationListUsingGET_success({
              payload: {
                clientConfigurationList: clientConfigurationList,
                baseUrl: window.location.origin + this.locationStrategy.getBaseHref(),
              },
            });
          }),
          catchError((error: any) => {
            return of(readClientConfigurationListUsingGET_failure({
              payload: {
                title: 'OAuth data missing',
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
      tap((action) => {
        console.log('readConfigurationDetailsUsingGET_success');
        let clientConfigurationList: Array<ClientConfiguration> = action.payload.clientConfigurationList;
        // let baseUrl: string = action.payload.baseUrl;
        console.log('clientConfigurationList Loaded', clientConfigurationList);
      }),
    );
  }, {dispatch: false});

  // forward as error action...
  readClientConfigurationListUsingGET_failure$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readClientConfigurationListUsingGET_failure),
      map((action) => {
        console.log('readClientConfigurationListUsingGET_failure');
        return showNotification({payload: action.payload});
      }),
    );
  });

  loginAction$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(loginAction),
      tap((action) => {
        console.log('authorizeAction for ', action.payload);

        if (!this.oidcSecurityService.getConfigurations().some((elem) => {
          return elem.configId == action.payload.configId;
        })) {
          console.error('no config found for ', action.payload.configId);
          console.error('available: ', this.oidcSecurityService.getConfigurations());
        }
        // see: https://nice-hill-002425310.azurestaticapps.net/docs/documentation/public-api
        this.oidcSecurityService.authorize(action.payload.configId); // this performs a browser redirect to the login page
      }),
    );
  }, {dispatch: false});

  logoutAction$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(logoutAction),
      tap((action) => {
        // see: https://nice-hill-002425310.azurestaticapps.net/docs/documentation/login-logout
        console.log('logoffAction...');
        // this.oidcSecurityService.logoff();
        this.oidcSecurityService.logoffLocal(); // TODO: add configId
        //this.oidcSecurityService.logoffAndRevokeTokens()
        //  .subscribe((result) => console.log(result));
      }),
    );
  }, {dispatch: false});

}
