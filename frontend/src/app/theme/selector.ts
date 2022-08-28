import {createFeatureSelector, createSelector} from '@ngrx/store';
import {featureKey, ThemeDetails} from './reducer';

export const selectUxFeature = createFeatureSelector<ThemeDetails>(featureKey);

export const isDarkTheme = createSelector(
  selectUxFeature,
  (state: ThemeDetails) => state.isDark,
);

