import {createAction, props} from '@ngrx/store';
import {NavState, ThemeDetails} from './reducer';
import {oAuthFeature} from '../const';
import {MatDrawerMode} from '@angular/material/sidenav';

const GROUP = '@app/' + oAuthFeature;

export const configureTheme = createAction(
  `${GROUP}/configureTheme`,
  props<{ payload: ThemeDetails }>(),
);

// see: https://medium.com/@karsonbraaten/create-an-angular-material-responsive-sidenav-directive-5f641c53b2be
export const setNavPosition = createAction(
  `${GROUP}/setNavPosition`,
  props<{ payload: { position: MatDrawerMode } }>(),
);

export const setNavState = createAction(
  `${GROUP}/setNavState`,
  props<{ payload: { navState: NavState } }>(),
);
