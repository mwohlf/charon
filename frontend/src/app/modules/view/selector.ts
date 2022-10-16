import {createFeatureSelector, createSelector} from '@ngrx/store';
import {ViewState} from './reducer';
import {viewFeature} from '../../shared/const';

export const selectViewFeature = createFeatureSelector<ViewState>(viewFeature);

export const selectCurrentTheme = createSelector(
  selectViewFeature,
  (state: ViewState) => state,
);

export const selectNavDrawMode = createSelector(
  selectViewFeature,
  (state: ViewState) => state.navDrawerMode,
);

export const selectNavState = createSelector(
  selectViewFeature,
  (state: ViewState) => state.navState,
);

