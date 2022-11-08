import {createFeatureSelector, createSelector} from '@ngrx/store';
import {OAuthState} from './reducer';
import {oAuthFeature} from '../../shared/const';

export const selectOAuthFeature = createFeatureSelector<OAuthState>(oAuthFeature);

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
  selectOAuthFeature,
  (state: OAuthState) => {
    return state.isAuthenticated;
  },
);

export const selectConfigId = createSelector(
  selectOAuthFeature,
  (state: OAuthState) => {
    return state.configId
  },
);


