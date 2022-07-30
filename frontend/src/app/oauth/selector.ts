import {createFeatureSelector, createSelector, Store} from '@ngrx/store';
import {featureKey, OAuthState} from './reducer';
import { ConfigurationDetails } from 'build/generated';

export const selectOAuthFeature = createFeatureSelector<OAuthState>(featureKey);

export const selectOAuthDetails = createSelector(
  selectOAuthFeature,
  (state: OAuthState) => {
    return state;
  },
);

export const isAuthenticated = createSelector(
  selectOAuthDetails,
  (state: OAuthState | undefined) => state?.isAuthenticated,
);


