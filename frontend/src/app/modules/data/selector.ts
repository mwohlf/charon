import {createFeatureSelector, createSelector} from '@ngrx/store';
import {DataState} from './reducer';
import {dataFeature} from '../../shared/const';

export const selectDataFeature = createFeatureSelector<DataState>(dataFeature);

export const selectRandomData = createSelector(
  selectDataFeature,
  (state: DataState) => {
    return state.randomData;
  },
);

