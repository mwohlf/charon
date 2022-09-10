import {createFeatureSelector, createSelector} from '@ngrx/store';
import {ThemeDetails} from './reducer';
import {viewFeature} from '../const';

export const selectViewFeature = createFeatureSelector<ThemeDetails>(viewFeature);

export const selectCurrentTheme = createSelector(
  selectViewFeature,
  (state: ThemeDetails) => state,
);

export const selectNavPosition = createSelector(
  selectViewFeature,
  (state: ThemeDetails) => state.navDrawerMode,
);

export const selectNavState = createSelector(
  selectViewFeature,
  (state: ThemeDetails) => state.navState,
);

