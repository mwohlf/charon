import {createFeatureSelector, createSelector} from '@ngrx/store';
import {featureKey, UxState} from './reducer';

export const selectUxFeature = createFeatureSelector<UxState>(featureKey);

export const isDarkTheme = createSelector(
  selectUxFeature,
  (state: UxState) => state.isDarkTheme,
);

