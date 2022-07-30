import {createAction, props} from '@ngrx/store';

export const showError = createAction(
  '@modules/error/showError',
  props<{ payload: ErrorDetails }>(),
);

export const clearError = createAction(
  '@modules/error/clearError',
);

export interface ErrorDetails {
  title: string;
  message: string;
  details: string;
}


