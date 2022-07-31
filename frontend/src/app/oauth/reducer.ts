import * as fromActions from './action';
import {Action, createReducer, on} from '@ngrx/store';
import {authorizeAction, logoffAction, receivedAccessToken} from './action';
import {ConfigState} from '../config/reducer';

export const featureKey = 'oAuthFeature';

export interface OAuthState {
  isAuthenticated: boolean;
  accessToken: string | undefined;
}

export const initialState: OAuthState = {
  isAuthenticated: false,
  accessToken: undefined,
};

const featureReducer = createReducer(
  initialState,
  on(fromActions.authorizeAction,
    (state: OAuthState) => {
      return {
        ...state, // keep the old state in case we are updating...
        isAuthenticated: true,
        accessToken: undefined,
      };
    },
  ),
  on(fromActions.receivedAccessToken,
    (state: OAuthState, {payload: accessToken}) => {
      return {
        ...state, // keep the old state in case we are updating...
        isAuthenticated: true,
        accessToken: accessToken,
      };
    },
  ),
  on(fromActions.logoffAction,
    (state: OAuthState) => {
      return {
        ...state, // keep the old state in case we are updating...
        isAuthenticated: false,
        accessToken: undefined,
      };
    },
  ),
);


export const reducer = (state: OAuthState | undefined, action: Action) => featureReducer(state, action);
