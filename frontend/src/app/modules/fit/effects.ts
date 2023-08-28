import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import {Observable, of} from 'rxjs';
import {catchError, map, mergeMap, tap} from 'rxjs/operators';

import {NGXLogger} from 'ngx-logger';

import {
  readFitDataSourcesUsingGET,
  readFitDataSourcesUsingGET_failure,
  readFitDataSourcesUsingGET_success,
} from './action';
import {showNotification} from '../notification/action';
import {DataAccessService} from 'build/generated';
import {Level, NotificationData} from '../notification/reducer';
import {FitDataSource} from 'build/generated/model/fit-data-source';

@Injectable()
export class Effects {

  constructor(
    private action$: Actions,
    private logger: NGXLogger,
    private dataAccessService: DataAccessService,
  ) {
  }

  // noinspection JSUnusedGlobalSymbols
  readFitDataSourcesUsingGET$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readFitDataSourcesUsingGET),
      tap((action) => {
        this.logger.info('readFitDataSourcesUsingGET...');
        this.logger.debug('<readFitDataSourcesUsingGET>', JSON.stringify(action));
      }),
      mergeMap((action: {}) => {
        return this.dataAccessService.readFitDataSources().pipe(
          map((fitDataSources: Array<FitDataSource>) => {
            return readFitDataSourcesUsingGET_success({
              payload: fitDataSources,
            });
          }),
          catchError((error: any) => {
            return of(readFitDataSourcesUsingGET_failure({
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
  readRandomDataUsingGET_failure$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readFitDataSourcesUsingGET_failure),
      tap((action) => {
        this.logger.debug('<readRandomDataUsingGET_failure>', JSON.stringify(action));
      }),
      map((action: { payload: NotificationData }) => {
        return showNotification({payload: action.payload});
      }),
    );
  });

}
