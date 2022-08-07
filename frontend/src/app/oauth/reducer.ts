import * as fromActions from './action';
import {Action, createReducer, on} from '@ngrx/store';
import {ClientConfiguration} from 'build/generated';
import {
  OpenIdConfiguration,
} from 'angular-auth-oidc-client/lib/config/openid-configuration';
import {LogLevel} from 'angular-auth-oidc-client';

export const SIMPLE_CONFIG = 'spring-oauth';

export interface OAuthState {
  configId: string | undefined;
  authState: string;
  isAuthenticated: boolean;
  openIdConfigurations: Array<OpenIdConfiguration>,
  userData: any;
  errorMessage: string | undefined;
}

export const initialState: OAuthState = {
  configId: undefined,
  authState: 'undefined',
  isAuthenticated: false,
  openIdConfigurations: [],
  userData: undefined,
  errorMessage: undefined,
};

const featureReducer = createReducer(
  initialState,

  on(fromActions.loginAction,
    (state: OAuthState, {payload: {configId: configId}}) => {
      return {
        ...state,
        configId: configId,
      };
    },
  ),

  on(fromActions.oauthEventAction,
    (state: OAuthState, {payload: payload}) => {
      const values = [
        'ConfigLoaded',
        'ConfigLoadingFailed',
        'CheckSessionReceived',
        'UserDataChanged',
        'NewAuthenticationResult',
        'TokenExpired',
        'IdTokenExpired',
        'SilentRenewStarted',
      ];
      console.log(' payload: ', payload);
      return {
        ...state,
        authState: values[payload.type],
      };
    },
  ),

  on(fromActions.oidcSecurityAction,
    (state: OAuthState, {payload: payload}) => {
      return {
        ...state, // keep the old state in case we are updating...
        configId: payload.configId,
        isAuthenticated: payload.isAuthenticated,
        userData: payload.userData,
        errorMessage: payload.errorMessage,
      };
    },
  ),


  on(fromActions.readClientConfigurationListUsingGET_success,
    (state: OAuthState, {payload: payload}) => {
      const openIdConfigurations: Array<OpenIdConfiguration> = payload.map(
        (element: ClientConfiguration) => {
          const result: OpenIdConfiguration = {
            configId: element.configId,
            authority: element.issuerUri,
            clientId: element.clientId,
            redirectUrl: 'http://127.0.0.1:4200/home',
            authWellknownEndpointUrl: undefined,
            postLogoutRedirectUri: window.location.origin,
            scope: 'openid profile email offline_access',
            responseType: 'code',
            silentRenew: true,
            useRefreshToken: true,
            logLevel: LogLevel.Debug,
            autoUserInfo: false,

            secureRoutes: [
              '/api',
              '/oauth2',
              'https://localhost/',
              'https://127.0.0.1/',
              'https://localhost:8080/',
              'https://127.0.0.1:8080/',
              'https://localhost:4200/',
              'https://127.0.0.1:4200/',
              'http://127.0.0.1:8081/oauth2/revoke',
            ],
          };
          return result;
        },
      );
      return {
        ...state, // keep the old state in case we are updating...
        openIdConfigurations: openIdConfigurations,
      };
    },
  ),
);


export const reducer = (state: OAuthState | undefined, action: Action) => featureReducer(state, action);
