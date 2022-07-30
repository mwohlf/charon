import {createAction, props} from '@ngrx/store';


// only create in the store
export const loginAction = createAction(
  '@app/oauth/login',
  props<{ payload: { isAuthenticated: boolean } }>(),
);

// only delete in the store
export const logoutAction = createAction(
  '@app/oauth/logout',
  props<{ payload: { isAuthenticated: boolean } }>(),
);
