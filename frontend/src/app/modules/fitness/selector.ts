import {createFeatureSelector, createSelector} from '@ngrx/store';
import {FitnessState} from './reducer';
import {fitFeature} from '../../shared/const';

export const selectFitFeature = createFeatureSelector<FitnessState>(fitFeature);

// noinspection JSUnusedGlobalSymbols
export const fitnessDataListElements = createSelector(
  selectFitFeature,
  (state: FitnessState) => {
    return state.fitnessDataList.fitnessDataListElements;
  },
);

export const selectFitnessDataItem = createSelector(
  selectFitFeature,
  (state: FitnessState) => {
    return state.fitnessItem.fitnessDataItem;
  },
);

export const selectFitnessDataItemId = createSelector(
  selectFitFeature,
  (state: FitnessState) => {
    return state.fitnessItem.fitnessDataItem?.id;
  },
);

