import {createAction, props} from '@ngrx/store';
import {ThemeDetails} from './reducer';

const GROUP = '@app/theme';

export const configureTheme = createAction(
  `${GROUP}/configureTheme`,
  props<{ payload: ThemeDetails }>(),
);
