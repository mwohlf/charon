import * as fromActions from './action';
import {Action, createReducer, on} from '@ngrx/store';

export const SIMPLE_CONFIG = 'simpleConfig';

export interface OAuthState {
  configId: string | undefined;
  authState: string;
  isAuthenticated: boolean;
  userData: any;
  errorMessage: string | undefined;
}

export const initialState: OAuthState = {
  configId: undefined,
  authState: 'undefined',
  isAuthenticated: false,
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
);


export const reducer = (state: OAuthState | undefined, action: Action) => featureReducer(state, action);
