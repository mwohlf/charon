import {createAction, props} from '@ngrx/store';


export interface ErrorDetails {
  title: string;
  message: string;
  details: string;
}

export const showError = createAction(
  '@app/error/showError',
  props<{ payload: ErrorDetails }>(),
);

export const clearError = createAction(
  '@app/error/clearError',
);


