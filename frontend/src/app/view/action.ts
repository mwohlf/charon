import {createAction, props} from '@ngrx/store';
import {ThemeDetails} from './reducer';
import {oAuthFeature} from '../const';

const GROUP = '@app/' + oAuthFeature;

export const configureTheme = createAction(
  `${GROUP}/configureTheme`,
  props<{ payload: ThemeDetails }>(),
);

// see: https://medium.com/@karsonbraaten/create-an-angular-material-responsive-sidenav-directive-5f641c53b2be
export const setNavPosition = createAction(
  `${GROUP}/setNavPosition`,
  props<{ payload: { position: string } }>(),
);
