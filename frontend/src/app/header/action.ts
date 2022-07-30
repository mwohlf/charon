import {createAction, props} from '@ngrx/store';


// only create in the store
export const enableHeaderFlag = createAction(
  '@app/header/enable',
  props<{ payload: { isEnabled: boolean } }>(),
);

// only delete in the store
export const disableHeaderFlag = createAction(
  '@app/header/disable',
  props<{ payload: { isEnabled: boolean } }>(),
);
