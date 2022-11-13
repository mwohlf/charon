import * as fromActions from './action';
import {Action, createReducer, on} from '@ngrx/store';
import {ClientConfiguration} from 'build/generated';
import {
  OpenIdConfiguration,
} from 'angular-auth-oidc-client/lib/config/openid-configuration';
import {HomeComponent} from '../../pages/home/home.component';
import {MainComponent} from '../../pages/main/main.component';
import {LoggerHolder} from '../../shared/logger-holder';


export const SIMPLE_CONFIG = 'spring-oauth';

// this needs to be updated if we update the oidc lib
export const eventList = [
  'ConfigLoaded',
  'CheckingAuth',
  'CheckingAuthFinished',
  'CheckingAuthFinishedWithError',
  'ConfigLoadingFailed',
  'CheckSessionReceived',
  'UserDataChanged',
  'NewAuthenticationResult',
  'TokenExpired',
  'IdTokenExpired',
  'SilentRenewStarted',
];

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
      LoggerHolder.logger.debug(`<oauthEventAction> payload: `, payload);
      // these values are copied and pasted from the last version of EventTypes
      // from the angular-auth-oidc-client lib, make sure to update
      // them when updating the lib!

      let eventString = eventList[payload.type];
      LoggerHolder.logger.debug(`<oauthEventAction> eventString: `, eventString);
      let result = {
        ...state,
        authState: eventString,
      };
      switch (eventString) {
        case 'UserDataChanged':
          result = {
            ...result,
            isAuthenticated: payload.value.userData != null,
          };
          break;
        case 'ConfigLoadingFailed':
        default:
          // not doing anything here
          break;
      }
      return result;
    },
  ),

  on(fromActions.oidcSecurityAction,
    (state: OAuthState, {payload: payload}) => {
      LoggerHolder.logger.debug(`<oidcSecurityAction> payload: `, payload);
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
      LoggerHolder.logger.debug(`<readClientConfigurationListUsingGET_success> payload: `, payload);
      const baseUrl = payload.baseUrl;
      const openIdConfigurations: Array<OpenIdConfiguration> = payload.clientConfigurationList.map(
        (element: ClientConfiguration) => {
          return {
            configId: element.configId,
            authority: element.issuerUri,
            clientId: element.clientId,
            redirectUrl: baseUrl + HomeComponent.ROUTER_PATH,
            postLogoutRedirectUri: baseUrl + MainComponent.ROUTER_PATH,
            // scope: 'openid profile email offline_access',
            scope: 'openid',
            responseType: 'code',
            silentRenew: true,
            useRefreshToken: false, // not provided by the spring-boot backend
            silentRenewUrl: baseUrl + 'assets/silent-renew.html',   // not sure this makes sense here...
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
