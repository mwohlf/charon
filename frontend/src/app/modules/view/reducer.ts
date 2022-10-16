import {Action, createReducer, on} from '@ngrx/store';
import * as fromActions from './action';
import {MatDrawerMode} from '@angular/material/sidenav';
import {themes} from './theme-list';


export declare type ThemeVariant = 'light' | 'dark'
export declare type NavState = 'opened' | 'closed'


// from https://github.com/angular/material.angular.io/blob/main/src/app/shared/theme-picker/theme-storage/theme-storage.ts
export type ThemeDetails = {
  displayName: string;
  name: string;
  variant: ThemeVariant;
}

export type NavDetails = {
  navDrawerMode: MatDrawerMode;  // 'over' | 'push' | 'side';
  navState: NavState;  // open or closed when in side mode
}

export type ViewState = ThemeDetails & NavDetails;

let theme = themes[0];
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
        navDrawerMode: payload.navDrawMode,
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

  on(fromActions.toggleMenu,
    (state: ViewState) => {
      let nextState: NavState = state.navState == 'opened'? 'closed' : 'opened'
      return {
        ...state,
        navState: nextState,
      };
    },
  ),

  on(fromActions.enableHeaderFlag,
    (state: ViewState, {payload: {isHeaderEnabled: isEnabled}}) => {
      return {
        ...state, // keep the old state in case we are updating...
        isHeaderEnabled: isEnabled,
      };
    },
  ),

  on(fromActions.disableHeaderFlag,
    (state: ViewState, {payload: {isHeaderEnabled: isEnabled}}) => {
      return {
        ...state, // keep the old state in case we are updating...
        isHeaderEnabled: isEnabled,
      };
    },
  ),

);

export function reducer(state: ViewState | undefined, action: Action): ViewState {
  return featureReducer(state, action);
}