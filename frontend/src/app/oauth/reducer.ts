import * as fromActions from './action';
import {Action, createReducer, on} from '@ngrx/store';
import {ClientConfiguration} from 'build/generated';
import {
  OpenIdConfiguration,
} from 'angular-auth-oidc-client/lib/config/openid-configuration';
import {LogLevel} from 'angular-auth-oidc-client';
import {environment} from '../../environments/environment';
import {HomeComponent} from '../components/home/home.component';
import {MainComponent} from '../components/main/main.component';


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
      console.log(` oauthEventAction, type: ${values[payload.type]}; payload: `, payload);
      return {
        ...state,
        authState: values[payload.type],
      };
    },
  ),

  on(fromActions.oidcSecurityAction,
    (state: OAuthState, {payload: payload}) => {
      console.log(` oidcSecurityAction, payload: `, payload);
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
      console.log(' readClientConfigurationListUsingGET_success, payload: ', payload);
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
            // see: https://github.com/damienbod/angular-auth-oidc-client/issues/788
            // https://nice-hill-002425310.azurestaticapps.net/docs/documentation/silent-renew
            // silentRenewUrl: baseUrl + HomeComponent.ROUTER_PATH,   // not sure this makes sense here...
            // http://127.0.0.1:8081/oauth2/authorize
            // ?client_id=public-client
            // &redirect_uri=http%3A%2F%2F127.0.0.1%3A4200%2Fcharon%2Fsilent-renew.html
            // &response_type=code
            // &scope=openid%20profile%20email%20offline_access
            // &nonce=08291e915ce68a5b9f2a196eee9a1f8391cSHXuCZ
            // &state=f87d3fa6505c3bc238d82745e9dd28f598Mo9sCQt
            // &code_challenge=6ZGBhBX3wA2u2CmgFBCOQpO07YhQnnATJzdLhjja0RI
            // &code_challenge_method=S256
            // &prompt=none
            silentRenewUrl: baseUrl + "assets/silent-renew.html",   // not sure this makes sense here...
            renewTimeBeforeTokenExpiresInSeconds: 15,
            // startCheckSession: false,
            logLevel: LogLevel.Debug,
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
