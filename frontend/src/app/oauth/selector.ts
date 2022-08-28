import {createFeatureSelector, createSelector, Store} from '@ngrx/store';
import {featureKey, OAuthState} from './reducer';
import {take} from 'rxjs';
import {
  OpenIdConfiguration,
} from 'angular-auth-oidc-client/lib/config/openid-configuration';
import {AppState} from '../app-shell.module';

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
  selectOAuthFeature,
  (state: OAuthState) => {
    return state.isAuthenticated;
  },
);


