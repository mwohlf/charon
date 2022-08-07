import {createAction, props} from '@ngrx/store';
import {LoginResponse, OidcClientNotification} from 'angular-auth-oidc-client';
import {ClientConfiguration} from 'build/generated';
import {ErrorDetails} from '../error/action';

export const readClientConfigurationListUsingGET = createAction(
  '@app/oauth/readClientConfigurationListUsingGET',
);

export const readClientConfigurationListUsingGET_success = createAction(
  '@app/config/readClientConfigurationListUsingGET_success',
  props<{ payload: Array<ClientConfiguration> }>(),
);

export const readClientConfigurationListUsingGET_failure = createAction(
  '@app/config/readClientConfigurationListUsingGET_failure',
  props<{ payload: ErrorDetails }>(),
);

// login with a specific auth issuer
export const loginAction = createAction(
  '@app/oauth/loginAction',
  props<{ payload: { configId: string } }>(),
);

export const logoutAction = createAction(
  '@app/oauth/logoffAction',
);

// fire events from the oidc service
export const oauthEventAction = createAction(
  '@app/oauth/oauthEventAction',
  props<{ payload: OidcClientNotification<any> }>(),
);

// fire events from the oidc service
export const oidcSecurityAction = createAction(
  '@app/oauth/oidcSecurityAction',
  props<{ payload: LoginResponse }>(),
);
