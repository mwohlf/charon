import * as fromActions from './action';
import {Action, createReducer, on} from '@ngrx/store';
import {loginAction, logoutAction} from './action';

export const featureKey = 'oAuthFeature';

export interface OAuthState {
  isAuthenticated: boolean;
}

export const initialState: OAuthState = {
  isAuthenticated: false,
};

const featureReducer = createReducer(
  initialState,
  on(fromActions.loginAction,
    (state: OAuthState, {payload: {isAuthenticated: isAuthenticated}}) => {
      return {
        ...state, // keep the old state in case we are updating...
        isAuthenticated: isAuthenticated,
      };
    },
  ),
  on(fromActions.logoutAction,
    (state: OAuthState, {payload: {isAuthenticated: isAuthenticated}}) => {
      return {
        ...state, // keep the old state in case we are updating...
        isAuthenticated: isAuthenticated,
      };
    },
  ),
);


export const reducer = (state: OAuthState | undefined, action: Action) => featureReducer(state, action);
