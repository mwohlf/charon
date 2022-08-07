import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType, ROOT_EFFECTS_INIT} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import {Observable, of} from 'rxjs';
import {catchError, map, mergeMap, tap} from 'rxjs/operators';

import {NGXLogger} from 'ngx-logger';

import {
  readConfigurationDetailsUsingGET,
  readConfigurationDetailsUsingGET_failure,
  readConfigurationDetailsUsingGET_success,
} from './action';
import {ErrorDetails, showError} from '../error/action';
import {LoggerHolder} from '../app-shell.module';
import {ConfigurationDetails, ConfigurationDetailsService } from 'build/generated';

@Injectable()
export class Effects {

  constructor(
    private configurationDetailsService: ConfigurationDetailsService,
    private logger: NGXLogger,
    private action$: Actions,
  ) {
  }

  ROOT_EFFECTS_INIT: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(ROOT_EFFECTS_INIT), // the trigger to start loading config
      tap((action) => {
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
  readConfigurationDetailsUsingGET$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readConfigurationDetailsUsingGET),
      mergeMap((action) => {
        console.log('readConfigurationDetailsUsingGET');
        return this.configurationDetailsService.readConfigurationDetails().pipe(
          map((configurationDetails: ConfigurationDetails) => {
            return readConfigurationDetailsUsingGET_success({
              payload: configurationDetails,
            });
          }),
          catchError((error: any) => {
            return of(readConfigurationDetailsUsingGET_failure({
              payload: {
                title: 'Config data missing',
                message: 'Config data can\'t be loaded.',
                details: JSON.stringify(error, null, 2),
              },
            }));
          }),
        );
      }),
    );
  });

  // config is ready and loaded
  readConfigurationDetailsUsingGET_success$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readConfigurationDetailsUsingGET_success),
      tap(action => {
        console.log('readConfigurationDetailsUsingGET_success');
        let configurationDetails: ConfigurationDetails = action.payload;
        //this.logger.updateConfig({
        // level: configDto.devmode?NgxLoggerLevel.TRACE:NgxLoggerLevel.WARN,
        // level: configDto.devmode?NgxLoggerLevel.TRACE:NgxLoggerLevel.WARN,
        // serverLogLevel: NgxLoggerLevel[configDto.serverLogLevel],
        // serverLoggingUrl: configDto.serverLoggingUrl,
        //})
        //LoggerHolder.logger = this.logger;
        this.logger.debug('Configuration Loaded');
        // let logLevel = NgxLoggerLevel[configDto.serverLogLevel];
        // this.logger.info("server log level is ", NgxLoggerLevel[logLevel]);
      }),
    );
  }, {dispatch: false});

  // forward as error action...
  readConfigurationDetailsUsingGET_failure$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readConfigurationDetailsUsingGET_failure),
      map((action: { payload: ErrorDetails }) => {
        console.log('readConfigurationDetailsUsingGET_failure');
        return showError({payload: action.payload});
      }),
    );
  });

}
