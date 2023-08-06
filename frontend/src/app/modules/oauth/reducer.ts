import * as fromActions from './action';
import {Action, createReducer, on} from '@ngrx/store';
import {ClientConfiguration} from 'build/generated';
import {
  OpenIdConfiguration,
} from 'angular-auth-oidc-client/lib/config/openid-configuration';
import {HomeComponent} from '../../pages/home/home.component';
import {LoggerHolder} from '../../shared/logger-holder';
import {EventTypes, LogLevel} from 'angular-auth-oidc-client';


export const SIMPLE_CONFIG = 'spring-oauth';

// this needs to be updated if we update the oidc lib

export interface OAuthState {
  configId: string | undefined;
  authState: string;
  isAuthenticated: boolean;
  openIdConfigurations: Array<OpenIdConfiguration>,
  userData: any;
  errorMessage: string | undefined;
  userName: string | undefined;
  roles: string[] | undefined;
}

export const initialState: OAuthState = {
  configId: undefined,
  authState: 'undefined',
  isAuthenticated: false,
  openIdConfigurations: [],
  userData: undefined,
  errorMessage: undefined,
  userName: undefined,
  roles: undefined,
};

const featureReducer = createReducer(
  initialState,

  // login action triggers the workflow
  on(fromActions.loginAction,
    (state: OAuthState, {payload: {configId: configId}}) => {
      return {
        ...state,
        configId: configId,
      };
    },
  ),

  // something coming from the oauth framework e.g. login success, token refresh etc.
  on(fromActions.oauthEventAction,
    (state: OAuthState, {payload: payload}) => {
      // these values are copied and pasted from the last version of EventTypes
      // from the angular-auth-oidc-client lib, make sure to update
      // them when updating the lib!

      let authState: string = EventTypes[payload.type];
      LoggerHolder.logger.debug(`<oauthEventAction> authState: ${authState}, payload: `, JSON.stringify(payload));
      let result = {
        ...state,
        authState: authState,
      };
      switch (payload.type) {
        case EventTypes.UserDataChanged:
          LoggerHolder.logger.info(`<oauthEventAction> UserDataChanged, payload: `, JSON.stringify(payload));
          result = {
            ...result,
            isAuthenticated: payload.value.userData != null,
          };
          break;
        case EventTypes.ConfigLoadingFailed:
        default:
          // not doing anything here
          LoggerHolder.logger.info(`<oauthEventAction> payload: `, JSON.stringify(payload));
          break;
      }
      return result;
    },
  ),

  //
  on(fromActions.oidcSecurityAction,
    (state: OAuthState, {payload: payload}) => {
      LoggerHolder.logger.info(`<oidcSecurityAction> payload: `, JSON.stringify(payload));
      return {
        ...state, // keep the old state in case we are updating...
        configId: payload.configId,
        isAuthenticated: payload.isAuthenticated,
        userData: payload.userData,
        errorMessage: payload.errorMessage,
        userName: payload.userData['userName'],
      };
    },
  ),

  // called after fetching the client list
  on(fromActions.readClientConfigurationListUsingGET_success,
    (state: OAuthState, {payload: payload}) => {
      LoggerHolder.logger.trace(`<readClientConfigurationListUsingGET_success> payload: `, JSON.stringify(payload));
      const baseUrl = payload.baseUrl;
      const openIdConfigurations: Array<OpenIdConfiguration> = payload.clientConfigurationList.map(
        (element: ClientConfiguration) => {
          return {
            configId: element.configId,
            authority: element.issuerUri,
            clientId: element.clientId,
            redirectUrl: baseUrl + HomeComponent.SPEC.route,
            postLogoutRedirectUri: element.postLogoutRedirectUri,
            scope: 'openid profile email offline_access',
            // scope: 'openid',
            responseType: 'code',
            silentRenew: true,
            useRefreshToken: false, // not provided by the spring-boot backend
            silentRenewUrl: baseUrl + 'assets/silent-renew.html',   // not sure if this makes sense here...
            renewTimeBeforeTokenExpiresInSeconds: 30,
            startCheckSession: true,
            logLevel: LogLevel.Debug,
            autoUserInfo: false, // authentication for this doesn't work yet
            secureRoutes: [
              '/api',   // all the backend endpoints
              // '/oauth2',
              element.issuerUri,
              // element.issuerUri + '/oauth2/revoke',
              element.issuerUri + '/userinfo',
            ],
          };
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
