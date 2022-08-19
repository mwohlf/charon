import {createAction, props} from '@ngrx/store';

const GROUP = '@app/header';


// only create in the store
export const enableHeaderFlag = createAction(
  `${GROUP}/enableHeaderFlag`,
  props<{ payload: { isEnabled: boolean } }>(),
);

// only delete in the store
export const disableHeaderFlag = createAction(
  `${GROUP}/disableHeaderFlag`,
  props<{ payload: { isEnabled: boolean } }>(),
);
