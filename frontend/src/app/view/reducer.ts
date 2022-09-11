import {Action, createReducer, on} from '@ngrx/store';
import * as fromActions from './action';
import {MatDrawerMode} from '@angular/material/sidenav';
import {themes} from './themelist';


export type ThemeVariant = 'light' | 'dark'
export type NavState = 'opened' | 'closed'



// from https://github.com/angular/material.angular.io/blob/main/src/app/shared/theme-picker/theme-storage/theme-storage.ts
export type ThemeDetails = {
  displayName: string;
  name: string;
  variant: ThemeVariant;
}

export type NavDetails = {
  navDrawerMode: MatDrawerMode;
  navState: NavState;
}

export type ViewState = ThemeDetails & NavDetails;

let theme = themes[0]
export const initialState: ViewState = {
  displayName: theme.displayName,
  name: theme.name,
  variant: 'light',
  navDrawerMode: 'side',
  navState: 'opened',
};


const featureReducer = createReducer(
  initialState,

  on(fromActions.setThemeDetails,
    (state: ViewState, {payload: payload}) => {
      return {
        ...state,
        ...payload,
      };
    },
  ),

  on(fromActions.setNavDrawMode,
    (state: ViewState, {payload: payload}) => {
      return {
        ...state,
        navDrawerMode: payload.drawMode,
      };
    },
  ),

  on(fromActions.setNavState,
    (state: ViewState, {payload: payload}) => {
      return {
        ...state,
        navState: payload.navState,
      };
    },
  ),

);

export function reducer(state: ViewState | undefined, action: Action): ViewState {
  return featureReducer(state, action);
}
