import {createFeatureSelector, createSelector} from '@ngrx/store';
import {FitState} from './reducer';
import {fitFeature} from '../../shared/const';

export const selectDataFeature = createFeatureSelector<FitState>(fitFeature);

// noinspection JSUnusedGlobalSymbols
export const selectDataSources = createSelector(
  selectDataFeature,
  (state: FitState) => {
    return state.dataSources.fitDataSources;
  },
);

