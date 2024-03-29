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
import {showNotification} from '../notification/action';
import {
  ConfigurationDetails,
  ConfigurationDetailsService,
} from 'build/generated';
import {Level, NotificationData} from '../notification/reducer';
import {LoggerHolder} from '../../shared/logger-holder';

@Injectable()
export class Effects {

  constructor(
    private action$: Actions,
    private logger: NGXLogger,
    private configurationDetailsService: ConfigurationDetailsService,
  ) {
  }

  // noinspection JSUnusedGlobalSymbols
  ROOT_EFFECTS_INIT: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(ROOT_EFFECTS_INIT), // the trigger to start loading config
      tap((action) => {
        this.logger.debug('<ROOT_EFFECTS_INIT>');
      }),
      tap((action: {}) => {
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

  // noinspection JSUnusedGlobalSymbols
  readConfigurationDetailsUsingGET$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readConfigurationDetailsUsingGET),
      tap((action) => {
        this.logger.debug('<readConfigurationDetailsUsingGET>', JSON.stringify(action));
      }),
      mergeMap((action: {}) => {
        return this.configurationDetailsService.readConfigurationDetails().pipe(
          map((configurationDetails: ConfigurationDetails) => {
            return readConfigurationDetailsUsingGET_success({
              payload: configurationDetails,
            });
          }),
          catchError((error: any) => {
            return of(readConfigurationDetailsUsingGET_failure({
              payload: {
                level: Level.Error,
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

  // noinspection JSUnusedGlobalSymbols
  readConfigurationDetailsUsingGET_success$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readConfigurationDetailsUsingGET_success),
      tap((action) => {
        this.logger.debug('<readConfigurationDetailsUsingGET_success>', JSON.stringify(action));
      }),
      map((action: { payload: ConfigurationDetails }) => {
        let configurationDetails: ConfigurationDetails = action.payload;
        //this.logger.updateConfig({
        // level: configDto.devmode?NgxLoggerLevel.TRACE:NgxLoggerLevel.WARN,
        // level: configDto.devmode?NgxLoggerLevel.TRACE:NgxLoggerLevel.WARN,
        // serverLogLevel: NgxLoggerLevel[configDto.serverLogLevel],
        // serverLoggingUrl: configDto.serverLoggingUrl,
        //})
        //LoggerHolder.logger = this.logger;
        this.logger.debug('<readConfigurationDetailsUsingGET_success> Configuration Loaded');
        // let logLevel = NgxLoggerLevel[configDto.serverLogLevel];
        // this.logger.info("server log level is ", NgxLoggerLevel[logLevel]);
        return showNotification({
          payload: {
            level: Level.Debug,
            title: 'Config data loaded',
            message: 'Config data have been loaded.',
            details: 'Config data have been loaded.',
          },
        });
      }),
    );
  });

  // noinspection JSUnusedGlobalSymbols
  readConfigurationDetailsUsingGET_failure$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readConfigurationDetailsUsingGET_failure),
      tap((action) => {
        this.logger.debug('<readConfigurationDetailsUsingGET_failure>', JSON.stringify(action));
      }),
      map((action: { payload: NotificationData }) => {
        return showNotification({payload: action.payload});
      }),
    );
  });

}
