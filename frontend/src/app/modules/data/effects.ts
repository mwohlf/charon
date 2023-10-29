import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import {Observable, of} from 'rxjs';
import {catchError, map, mergeMap, tap} from 'rxjs/operators';

import {NGXLogger} from 'ngx-logger';

import {
  readProtectedDataUsingGET,
  readProtectedDataUsingGET_failure,
  readProtectedDataUsingGET_success,
} from './action';
import {showNotification} from '../notification/action';
import {DataAccessService, ProtectedData} from 'build/generated';
import {Level, NotificationData} from '../notification/reducer';

@Injectable()
export class Effects {

  constructor(
    private action$: Actions,
    private logger: NGXLogger,
    private dataAccessService: DataAccessService,
  ) {
  }

  // the config loading action
  // noinspection JSUnusedGlobalSymbols
  readProtectedDataUsingGET$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readProtectedDataUsingGET),
      tap((action) => {
        this.logger.info('readProtectedDataUsingGET...');
        this.logger.debug('<readProtectedDataUsingGET>', JSON.stringify(action));
      }),
      mergeMap((action: {}) => {
        return this.dataAccessService.readProtectedData().pipe(
          map((protectedData: ProtectedData) => {
            return readProtectedDataUsingGET_success({
              payload: protectedData,
            });
          }),
          catchError((error: any) => {
            return of(readProtectedDataUsingGET_failure({
              payload: {
                level: Level.Error,
                title: 'Data missing',
                message: 'Data can\'t be loaded.',
                details: JSON.stringify(error, null, 2),
              },
            }));
          }),
        );
      }),
    );
  });


  // forward as error action...
  // noinspection JSUnusedGlobalSymbols
  readProtectedDataUsingGET_failure$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readProtectedDataUsingGET_failure),
      tap((action) => {
        this.logger.debug('<readProtectedDataUsingGET_failure>', JSON.stringify(action));
      }),
      map((action: { payload: NotificationData }) => {
        return showNotification({payload: action.payload});
      }),
    );
  });

}
