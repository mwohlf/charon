import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType, ROOT_EFFECTS_INIT} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import {Observable, of} from 'rxjs';
import {catchError, map, mergeMap, tap} from 'rxjs/operators';

import {NGXLogger, NgxLoggerLevel} from 'ngx-logger';
import {ConfigurationDetails, ConfigurationDetailsService} from 'build/generated';
import {
  readConfigurationDetailsUsingGET, readConfigurationDetailsUsingGET_failure,
  readConfigurationDetailsUsingGET_success,
} from './action';
import {showError} from '../error/action';

// public logging holder for static contexts
export class LoggerHolder {
  public static logger: any = console;
}

@Injectable()
export class Effects {

  constructor(
    private configurationDetailsService: ConfigurationDetailsService,
    private logger: NGXLogger,
    private action$: Actions,
  ) {
  }

  rootEffectsInit: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(ROOT_EFFECTS_INIT),
      tap(() => {
        // just to get rid of the console logging as early as possible
        LoggerHolder.logger = this.logger;
      }),
      mergeMap(() => {
        return [
          readConfigurationDetailsUsingGET(),
        ];
      }),
    );
  });

  // the config loading action
  readConfigurationUsingGET$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readConfigurationDetailsUsingGET),
      mergeMap(() => {
        return this.configurationDetailsService.readConfigurationDetails().pipe(
          map((configurationDetails: ConfigurationDetails) => {
            return readConfigurationDetailsUsingGET_success({payload: configurationDetails});
          }),
          catchError((error: Error) => {
            return of(readConfigurationDetailsUsingGET_failure({payload: error}));
          }),
        );
      }),
    );
  });

  // config is ready and loaded
  readConfigurationUsingGET_success$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readConfigurationDetailsUsingGET_success),
      tap(action => {
        let configurationDetails: ConfigurationDetails = action.payload;
        //this.logger.updateConfig({
          // level: configDto.devmode?NgxLoggerLevel.TRACE:NgxLoggerLevel.WARN,
          // level: configDto.devmode?NgxLoggerLevel.TRACE:NgxLoggerLevel.WARN,
          // serverLogLevel: NgxLoggerLevel[configDto.serverLogLevel],
          // serverLoggingUrl: configDto.serverLoggingUrl,
        //})
        LoggerHolder.logger = this.logger;
        this.logger.debug("Configuration Loaded");
        // let logLevel = NgxLoggerLevel[configDto.serverLogLevel];
        // this.logger.info("server log level is ", NgxLoggerLevel[logLevel]);
      }),
    );
  }, {dispatch: false});

  // forward as error action...
  readConfigurationUsingGET_failed$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readConfigurationDetailsUsingGET_failure),
      map(action => {
        return showError({payload: action.payload});
      }),
    );
  });

}
