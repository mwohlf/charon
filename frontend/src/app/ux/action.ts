import {createAction, props} from '@ngrx/store';
import {ThemeUpdate} from './reducer';

const GROUP = '@app/ux';

export const configureTheme = createAction(
  `${GROUP}/configureTheme`,
  props<{ payload: ThemeUpdate }>(),
);

export const toggleDarkModeAction = createAction(
  `${GROUP}/toggleDarkModeAction`,
);
