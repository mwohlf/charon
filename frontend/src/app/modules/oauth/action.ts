import {createAction, props} from '@ngrx/store';
import {LoginResponse, OidcClientNotification} from 'angular-auth-oidc-client';
import {ClientConfiguration} from 'build/generated';
import {NotificationData} from '../notification/reducer';
import {oAuthFeature} from '../../shared/const';

const GROUP = '@app/' + oAuthFeature;

export const readClientConfigurationListUsingGET = createAction(
  `${GROUP}/readClientConfigurationListUsingGET`,
);

export const readClientConfigurationListUsingGET_success = createAction(
  `${GROUP}/readClientConfigurationListUsingGET_success`,
  props<{
    payload: {
      clientConfigurationList: Array<ClientConfiguration>,
      baseUrl: string,
    }
  }>(),
);

export const readClientConfigurationListUsingGET_failure = createAction(
  `${GROUP}/readClientConfigurationListUsingGET_failure`,
  props<{ payload: NotificationData }>(),
);

// login with a specific auth issuer
export const loginAction = createAction(
  `${GROUP}/loginAction`,
  props<{ payload: { configId: string } }>(),
);

export const logoutAction = createAction(
  `${GROUP}/logoutAction`,
);

// fire events from the oidc service
export const oauthEventAction = createAction(
  `${GROUP}/oauthEventAction`,
  props<{ payload: OidcClientNotification<any> }>(),
);

// fire events from the oidc service
export const oidcSecurityAction = createAction(
  `${GROUP}/oidcSecurityAction`,
  props<{ payload: LoginResponse }>(),
);
