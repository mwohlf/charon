import * as fromActions from './action';
import {Action, createReducer, on} from '@ngrx/store';
import {authorizeAction, logoffAction} from './action';

export const featureKey = 'oAuthFeature';

export interface OAuthState {
  isAuthenticated: boolean;
}

export const initialState: OAuthState = {
  isAuthenticated: false,
};

const featureReducer = createReducer(
  initialState,
  on(fromActions.authorizeAction,
    (state: OAuthState) => {
      return {
        ...state, // keep the old state in case we are updating...
        isAuthenticated: true,
      };
    },
  ),
  on(fromActions.logoffAction,
    (state: OAuthState) => {
      return {
        ...state, // keep the old state in case we are updating...
        isAuthenticated: false,
      };
    },
  ),
);


export const reducer = (state: OAuthState | undefined, action: Action) => featureReducer(state, action);
