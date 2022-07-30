import {createAction, props} from '@ngrx/store';

export const OAUTH_INITIALIZED = "@app/oauth/initialization_success";

export const authorizeAction = createAction('@app/oauth/login');

export const logoffAction = createAction('@app/oauth/logout');
