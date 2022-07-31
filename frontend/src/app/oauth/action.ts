import {createAction, props} from '@ngrx/store';

export const OAUTH_INITIALIZED = "@app/oauth/initialization_success";

export const authorizeAction = createAction('@app/oauth/authorizeAction');

export const receivedAccessToken = createAction(
  '@app/oauth/receivedAccessToken',
  props<{ payload: string }>()
);

export const logoffAction = createAction('@app/oauth/logoffAction');
