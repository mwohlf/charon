import {Action, createReducer, on} from '@ngrx/store';
import * as fromActions from './action';
import {THEMES} from './theme-picker';

export const featureKey = 'themeFeature';


// from https://github.com/angular/material.angular.io/blob/main/src/app/shared/theme-picker/theme-storage/theme-storage.ts
export interface ThemeDetails {
  name: string;
  displayName?: string;
  accent: string;
  primary: string;
  isDark?: boolean;
}

const initialState: ThemeDetails = THEMES[0];


const featureReducer = createReducer(
  initialState,

  on(fromActions.configureTheme,
    (state: ThemeDetails, {payload: payload}) => {
      return {
        ...payload,
        isDarkTheme: (payload.isDark !== undefined) ? payload.isDark : state.isDark,
      };
    },
  ),

  on(fromActions.toggleDarkMode,
    (state: ThemeDetails) => {
      return {
        ...state,
        isDarkTheme: !state.isDark,
      };
    },
  ),
);

export function reducer(state: ThemeDetails | undefined, action: Action): ThemeDetails {
  return featureReducer(state, action);
}
