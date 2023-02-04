import * as fromActions from './action';
import {Action, createReducer, on} from '@ngrx/store';
import {ClientConfiguration} from 'build/generated';
import {
  OpenIdConfiguration,
} from 'angular-auth-oidc-client/lib/config/openid-configuration';
import {HomeComponent} from '../../pages/home/home.component';
import {LoggerHolder} from '../../shared/logger-holder';
import {EventTypes} from 'angular-auth-oidc-client';


export const SIMPLE_CONFIG = 'spring-oauth';

// this needs to be updated if we update the oidc lib

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
      // these values are copied and pasted from the last version of EventTypes
      // from the angular-auth-oidc-client lib, make sure to update
      // them when updating the lib!

      let eventString = EventTypes[payload.type];
      LoggerHolder.logger.debug(`<oauthEventAction> eventString: ${eventString}, payload: `, JSON.stringify(payload));
      let result = {
        ...state,
        authState: eventString,
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
          break;
      }
      return result;
    },
  ),

  on(fromActions.oidcSecurityAction,
    (state: OAuthState, {payload: payload}) => {
      LoggerHolder.logger.info(`<oidcSecurityAction> payload: `, JSON.stringify(payload));
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
            // scope: 'openid profile email offline_access',
            scope: 'openid',
            responseType: 'code',
            silentRenew: true,
            useRefreshToken: false, // not provided by the spring-boot backend
            silentRenewUrl: baseUrl + 'assets/silent-renew.html',   // not sure if this makes sense here...
            renewTimeBeforeTokenExpiresInSeconds: 15,
            // startCheckSession: false,
            // logLevel: LogLevel.Debug,
            // autoUserInfo: true,
            autoUserInfo: false,
            secureRoutes: [
              '/api',
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
