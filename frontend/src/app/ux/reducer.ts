import {Action, createReducer, on} from '@ngrx/store';
import * as fromActions from './action';

export const featureKey = 'uxFeature';

export interface UxState {
  isDarkTheme: boolean;
}

const initialState: UxState = {
  isDarkTheme: false,
};

export interface ThemeUpdate {
  isDarkTheme: boolean | undefined;
}


const featureReducer = createReducer(
  initialState,

  on(fromActions.configureTheme,
    (state: UxState, {payload: payload}) => {
      return {
        isDarkTheme: (payload.isDarkTheme !== undefined) ? payload.isDarkTheme : state.isDarkTheme,
      };
    },
  ),

  on(fromActions.toggleDarkModeAction,
    (state: UxState) => {
      return {
        isDarkTheme: !state.isDarkTheme,
      };
    },
  ),
);

export function reducer(state: UxState | undefined, action: Action): UxState {
  return featureReducer(state, action);
}
