import {createFeatureSelector, createSelector, Store} from '@ngrx/store';
import {featureKey, ConfigState} from './reducer';
import { ConfigurationDetails } from 'build/generated';

export const selectConfigFeature = createFeatureSelector<ConfigState>(featureKey);

export const selectConfigData = createSelector(
  selectConfigFeature,
  (state: ConfigState) => {
    return state.config;
  },
);

export const selectVersion = createSelector(
  selectConfigData,
  (config: ConfigurationDetails | undefined) => config?.version,
);

export const selectTimestamp = createSelector(
  selectConfigData,
  (config: ConfigurationDetails | undefined) => config?.timestamp,
);

export const selectName = createSelector(
  selectConfigData,
  (config: ConfigurationDetails | undefined) => config?.name,
);

