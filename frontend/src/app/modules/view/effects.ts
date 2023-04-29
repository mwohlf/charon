import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType, ROOT_EFFECTS_INIT} from '@ngrx/effects';
import {NGXLogger} from 'ngx-logger';
import {StyleManager} from './style-manager';
import {Observable} from 'rxjs';
import {setThemeDetails} from './action';
import {Action} from '@ngrx/store';
import {map, tap} from 'rxjs/operators';
import {initialState, ThemeVariant} from './reducer';

@Injectable()
export class Effects {

  private readonly browserThemeVariant: ThemeVariant;

  constructor(
    private action$: Actions,
    private logger: NGXLogger,
    private styleManager: StyleManager,
  ) {
    // Initially check if dark mode is enabled on system
    const darkModeOn =
      window.matchMedia &&
      window.matchMedia('(prefers-color-scheme: dark)').matches;
    if (darkModeOn) {
      this.browserThemeVariant = 'dark';
    } else {
      this.browserThemeVariant = 'light';
    }
    this.logger.debug('<view init> browserThemeVariant:', this.browserThemeVariant);
  }

  ROOT_EFFECTS_INIT: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(ROOT_EFFECTS_INIT), // the trigger to start loading config
      tap((action) => {
        this.logger.debug('<ROOT_EFFECTS_INIT> setting style', action);
      }),
      tap((_) => {
        // this.styleManager.setStyle('theme', `${initialState.name}-${initialState.variant}.css`);
      }),
      map(() => {
        return setThemeDetails({
          payload: {
            name: initialState.name,
            variant: this.browserThemeVariant,
            displayName: initialState.displayName,
          },
        });
      }),
    );
  }, {dispatch: true});

  configureTheme$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(setThemeDetails),
      tap((action) => {
        this.logger.debug('<setThemeDetails> setting style, triggered by setThemeDetails, action is:', action);
      }),
      tap((action) => {
        this.styleManager.setStyle('theme', `${action.payload.name}-${action.payload.variant}.css`);
      }),
    );
  }, {dispatch: false});

}
