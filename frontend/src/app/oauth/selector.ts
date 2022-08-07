import {createFeatureSelector, createSelector, Store} from '@ngrx/store';
import {OAuthState} from './reducer';
import {featureKey} from './oauth.module';
import {Observable, take} from 'rxjs';
import {
  OpenIdConfiguration
} from 'angular-auth-oidc-client/lib/config/openid-configuration';
import {AppState} from '../app-shell.module';
import {map} from 'rxjs/operators';

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

// a function to retrieve the state in sync
export function getOpenIdConfigurations(store: Store<AppState>): Array<OpenIdConfiguration> {
  var result: Array<OpenIdConfiguration> = [];
  store.select(selectOpenIdConfigurations).pipe(
    take(1),
  ).subscribe(
    next => {
      result = next || [];
    },
  );
  return result;
}

export const isAuthenticated = createSelector(
  selectOAuthDetails,
  (state: OAuthState) => state.isAuthenticated,
);


