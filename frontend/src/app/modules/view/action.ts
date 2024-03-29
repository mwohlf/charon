import {createAction, props} from '@ngrx/store';
import {Breakpoint, NavState, ThemeDetails} from './reducer';
import {viewFeature} from '../../shared/const';
import {MatDrawerMode} from '@angular/material/sidenav';

const GROUP: string = '@app/' + viewFeature;


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

export const setBreakpoint = createAction(
  `${GROUP}/setBreakpoint`,
  props<{ payload: { breakpoint: Breakpoint } }>(),
);

export const toggleMenu = createAction(
  `${GROUP}/toggleMenu`,
);

// only create in the store
export const enableHeaderFlag = createAction(
  `${GROUP}/enableHeaderFlag`,
);

// only delete in the store
export const disableHeaderFlag = createAction(
  `${GROUP}/disableHeaderFlag`,
);
