import {createFeatureSelector, createSelector} from '@ngrx/store';
import {ConfigState} from './reducer';
import {ConfigurationDetails} from 'build/generated';
import {configFeature} from '../../shared/const';

export const selectConfigFeature = createFeatureSelector<ConfigState>(configFeature);

export const selectConfigurationDetails = createSelector(
  selectConfigFeature,
  (state: ConfigState) => {
    return state.config;
  },
);

export const selectVersion = createSelector(
  selectConfigurationDetails,
  (config: ConfigurationDetails | undefined) => config?.version,
);

export const selectTimestamp = createSelector(
  selectConfigurationDetails,
  (config: ConfigurationDetails | undefined) => config?.timestamp,
);

export const selectName = createSelector(
  selectConfigurationDetails,
  (config: ConfigurationDetails | undefined) => config?.name,
);

