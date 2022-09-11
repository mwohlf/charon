import {createAction, props} from '@ngrx/store';
import {NavState, ThemeDetails} from './reducer';
import {oAuthFeature} from '../const';
import {MatDrawerMode} from '@angular/material/sidenav';

const GROUP = '@app/' + oAuthFeature;

export const setThemeDetails = createAction(
  `${GROUP}/configureTheme`,
  props<{ payload: ThemeDetails }>(),
);

// see: https://medium.com/@karsonbraaten/create-an-angular-material-responsive-sidenav-directive-5f641c53b2be
export const setNavDrawMode = createAction(
  `${GROUP}/setNavPosition`,
  props<{ payload: { navDrawMode: MatDrawerMode } }>(),
);

export const setNavState = createAction(
  `${GROUP}/setNavState`,
  props<{ payload: { navState: NavState } }>(),
);

export const toggleMenu = createAction(
  `${GROUP}/toggleMenu`,
);
