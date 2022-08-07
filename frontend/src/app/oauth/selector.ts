import {createFeatureSelector, createSelector, Store} from '@ngrx/store';
import {OAuthState} from './reducer';
import {featureKey} from './oauth.module';

export const selectOAuthFeature = createFeatureSelector<OAuthState>(featureKey);

export const selectOAuthDetails = createSelector(
  selectOAuthFeature,
  (state: OAuthState) => {
    return state;
  },
);

export const selectOpenIdConfigurations = createSelector(
  selectOAuthFeature,
  (state: OAuthState) => {
    return state.openIdConfigurations;
  },
);


export const isAuthenticated = createSelector(
  selectOAuthDetails,
  (state: OAuthState) => state.isAuthenticated,
);


