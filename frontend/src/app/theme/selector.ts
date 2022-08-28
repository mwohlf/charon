import {createFeatureSelector, createSelector} from '@ngrx/store';
import {featureKey, ThemeDetails} from './reducer';

export const selectThemeFeature = createFeatureSelector<ThemeDetails>(featureKey);

export const selectCurrentTheme = createSelector(
  selectThemeFeature,
  (state: ThemeDetails) => state,
);

export const isDarkTheme = createSelector(
  selectThemeFeature,
  (state: ThemeDetails) => state.isDark,
);

