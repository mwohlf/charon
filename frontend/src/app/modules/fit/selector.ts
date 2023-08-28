import {createFeatureSelector, createSelector} from '@ngrx/store';
import {FitState} from './reducer';
import {fitFeature} from '../../shared/const';

export const selectFitFeature = createFeatureSelector<FitState>(fitFeature);

// noinspection JSUnusedGlobalSymbols
export const selectFitDataSources = createSelector(
  selectFitFeature,
  (state: FitState) => {
    return state.dataSources.fitDataSources;
  },
);

