import {Action, createReducer, on} from '@ngrx/store';
import * as fromActions from './action';
import {MatDrawerMode} from '@angular/material/sidenav';


export type ThemeVariant = 'light' | 'dark'
export type NavState = 'opened' | 'closed'

// from https://github.com/angular/material.angular.io/blob/main/src/app/shared/theme-picker/theme-storage/theme-storage.ts
export interface ThemeDetails {
  displayName: string;
  name: string;
  variant: ThemeVariant;
  navDrawerMode: MatDrawerMode;
  navState: NavState;
}

export const initialState: ThemeDetails = {
  displayName: 'Deep Purple & Amber',
  name: 'deeppurple-amber',
  variant: 'light',
  navDrawerMode: 'side',
  navState: 'opened',
};


const featureReducer = createReducer(
  initialState,

  on(fromActions.configureTheme,
    (state: ThemeDetails, {payload: payload}) => {
      return {
        ...state,
        ...payload,
      };
    },
  ),

  on(fromActions.setNavPosition,
    (state: ThemeDetails, {payload: payload}) => {
      return {
        ...state,
        navPosition: payload.position,
      };
    },
  ),

  on(fromActions.setNavState,
    (state: ThemeDetails, {payload: payload}) => {
      return {
        ...state,
        navState: payload.navState,
      };
    },
  ),

);

export function reducer(state: ThemeDetails | undefined, action: Action): ThemeDetails {
  return featureReducer(state, action);
}
