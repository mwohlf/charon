import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType, ROOT_EFFECTS_INIT} from '@ngrx/effects';
import {NGXLogger} from 'ngx-logger';
import {StyleManager} from './style-manager';
import {Observable} from 'rxjs';
import {configureTheme} from './action';
import {Action} from '@ngrx/store';
import {mergeMap, tap} from 'rxjs/operators';
import {LoggerHolder} from '../app-shell.module';
import {readConfigurationDetailsUsingGET} from '../config/action';
import {initialState} from './reducer';

@Injectable()
export class Effects {

  constructor(
    private styleManager: StyleManager,
    private logger: NGXLogger,
    private action$: Actions,
  ) {
  }

  ROOT_EFFECTS_INIT: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(ROOT_EFFECTS_INIT), // the trigger to start loading config
      tap((action) => {
        console.log('configureTheme');
        this.styleManager.setStyle('theme', `${initialState.name}-${initialState.variant}.css`);
      }),
    );
  }, {dispatch: false});

  configureTheme$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(configureTheme),
      tap((action) => {
        console.log('configureTheme');
        this.styleManager.setStyle('theme', `${action.payload.name}-${action.payload.variant}.css`);
      }),
    );
  }, {dispatch: false});

}
