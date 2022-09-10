import {Action, createReducer, on} from '@ngrx/store';
import * as fromActions from './action';


export type NavPosition = 'side' | 'over'
export type ThemeVariant = 'light' | 'dark'

// from https://github.com/angular/material.angular.io/blob/main/src/app/shared/theme-picker/theme-storage/theme-storage.ts
export interface ThemeDetails {
  displayName: string;
  name: string;
  variant: ThemeVariant;
  navPosition: NavPosition;
}

export const initialState: ThemeDetails = {
  displayName: 'Deep Purple & Amber',
  name: 'deeppurple-amber',
  variant: 'light',
  navPosition: 'side',
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
);

export function reducer(state: ThemeDetails | undefined, action: Action): ThemeDetails {
  return featureReducer(state, action);
}
