import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType, ROOT_EFFECTS_INIT} from '@ngrx/effects';
import {
  LoginResponse,
  OidcClientNotification,
  OidcSecurityService,
  PublicEventsService,
} from 'angular-auth-oidc-client';
import {Observable, of, withLatestFrom} from 'rxjs';
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
import {NGXLogger} from 'ngx-logger';
import {selectOAuthFeature} from './selector';
import {OAuthState} from './reducer';

@Injectable()
export class Effects {

  constructor(
    private action$: Actions,
    private logger: NGXLogger,
    private locationStrategy: LocationStrategy,
    private configurationDetailsService: ConfigurationDetailsService,
    private oidcSecurityService: OidcSecurityService,
    private eventService: PublicEventsService,
    private store: Store<AppState>,
  ) {

    // this reads the callback url params from oauth,
    // apparently we are only supposed to call this once on initial load,
    // or on returning from the auth redirect,
    // then the magic happens and the oauth lib's state machine is doing its thing
    this.oidcSecurityService
      .checkAuth()
      .subscribe((next: LoginResponse) => {
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
        this.logger.debug('<ROOT_EFFECTS_INIT> boot up oauth, triggered by ROOT_EFFECTS_INIT', action);
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
      tap((action) => {
        this.logger.debug('<readClientConfigurationListUsingGET>', action);
      }),
      mergeMap((action) => {
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
  // this is just for logging, the reducer processes the payload from the action
  readClientConfigurationListUsingGET_success$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readClientConfigurationListUsingGET_success),
      tap((action) => {
        this.logger.debug('<readClientConfigurationListUsingGET_success>', action);
      }),
      //tap((action) => {
      //  let clientConfigurationList: Array<ClientConfiguration> = action.payload.clientConfigurationList;
      //  // let baseUrl: string = action.payload.baseUrl;
      //  console.log('clientConfigurationList Loaded', clientConfigurationList);
      //}),
    );
  }, {dispatch: false});

  // forward as error action...
  readClientConfigurationListUsingGET_failure$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readClientConfigurationListUsingGET_failure),
      tap((action) => {
        this.logger.debug('<readClientConfigurationListUsingGET_failure>', action);
      }),
      map((action) => {
        return showNotification({payload: action.payload});
      }),
    );
  });

  loginAction$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(loginAction),
      tap((action) => {
        this.logger.debug('<loginAction>', action);
      }),
      tap((action) => {
        // check if the configId is in our set
        if (!this.oidcSecurityService.getConfigurations().some((elem) => {
          return elem.configId == action.payload.configId;
        })) {
          this.logger.error('<loginAction> no config found for ', action.payload.configId);
          this.logger.error('<loginAction> available configs are : ', this.oidcSecurityService.getConfigurations());
        } else {
          // see: https://nice-hill-002425310.azurestaticapps.net/docs/documentation/public-api
          this.oidcSecurityService.authorize(action.payload.configId); // this performs a browser redirect to the login page
        }
      }),
    );
  }, {dispatch: false});

  logoutAction$: Observable<[Action, OAuthState]> = createEffect(() => {
    return this.action$.pipe(
      ofType(logoutAction),
      tap((action) => {
        this.logger.debug('<logoutAction>', action);
      }),
      withLatestFrom(this.store.select(selectOAuthFeature)),
      tap(([action, oAuthFeature]) => {
        this.logger.debug('<logoutAction> for configId:', oAuthFeature.configId);
        // see: https://nice-hill-002425310.azurestaticapps.net/docs/documentation/login-logout
        // this.oidcSecurityService.logoff();
        // this.oidcSecurityService.logoffLocal(); // TODO: add configId
        this.oidcSecurityService.logoffAndRevokeTokens().subscribe(() => {
          this.logger.debug('<logoffAndRevokeTokens> returned');
          var authority = oAuthFeature.openIdConfigurations.find(
            element => element.configId === oAuthFeature.configId,
          )?.authority || 'logout';
          window.location.href = authority + '/logout';
        });
      }),
    );
  }, {dispatch: false});

}
