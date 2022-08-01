import {createAction, props} from '@ngrx/store';
import {LoginResponse, OidcClientNotification} from 'angular-auth-oidc-client';
import {ErrorDetails} from '../error/action';
import { ClientConfiguration } from 'build/generated';

export const loginAction = createAction(
  '@app/oauth/loginAction',
  props<{ payload: { configId: string } }>(),
);

export const readClientConfigurationListUsingGET = createAction(
  '@app/oauth/readClientConfigurationListUsingGET',
);

export const readClientConfigurationListUsingGET_success = createAction(
  '@app/config/readClientConfigurationListUsingGET_success',
  props<{ payload: Array<ClientConfiguration> }>()
);

export const readClientConfigurationListUsingGET_failure = createAction(
  '@app/config/readClientConfigurationListUsingGET_failure',
  props<{ payload: ErrorDetails }>()
);

export const logoutAction = createAction(
  '@app/oauth/logoffAction'
);

export const oauthEventAction = createAction(
  '@app/oauth/oauthEventAction',
  props<{ payload: OidcClientNotification<any> }>(),
);

export const oidcSecurityAction = createAction(
  '@app/oauth/oidcSecurityAction',
  props<{ payload: LoginResponse }>(),
);
