import {createAction, props} from '@ngrx/store';

export const showError = createAction(
  '@modules/error/showError',
  props<{ payload: Error }>(),
);

export const clearError = createAction(
  '@modules/error/clearError',
);
