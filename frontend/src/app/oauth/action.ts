import {createAction, props} from '@ngrx/store';
import {LoginResponse, OidcClientNotification} from 'angular-auth-oidc-client';

export const loginAction = createAction(
  '@app/oauth/loginAction',
  props<{ payload: { configId: string } }>(),
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
