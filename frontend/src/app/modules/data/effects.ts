import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import {Observable, of} from 'rxjs';
import {catchError, map, mergeMap, tap} from 'rxjs/operators';

import {NGXLogger} from 'ngx-logger';

import {
  readRandomDataUsingGET,
  readRandomDataUsingGET_failure,
  readRandomDataUsingGET_success,
} from './action';
import {showNotification} from '../notification/action';
import {RandomData, DataAccessService} from 'build/generated';
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
  readRandomDataUsingGET$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readRandomDataUsingGET),
      tap((action) => {
        this.logger.info('readRandomDataUsingGET...')
        this.logger.debug('<readRandomDataUsingGET>', JSON.stringify(action));
      }),
      mergeMap((action: {}) => {
        return this.dataAccessService.readRandomData().pipe(
          map((randomData: RandomData) => {
            return readRandomDataUsingGET_success({
              payload: randomData,
            });
          }),
          catchError((error: any) => {
            return of(readRandomDataUsingGET_failure({
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
  readRandomDataUsingGET_failure$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readRandomDataUsingGET_failure),
      tap((action) => {
        this.logger.debug('<readRandomDataUsingGET_failure>', JSON.stringify(action));
      }),
      map((action: { payload: NotificationData }) => {
        return showNotification({payload: action.payload});
      }),
    );
  });

}
