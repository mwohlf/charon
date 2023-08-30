import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import {Observable, of} from 'rxjs';
import {catchError, map, mergeMap, tap} from 'rxjs/operators';

import {NGXLogger} from 'ngx-logger';

import {
  readFitnessDataItemUsingGET,
  readFitnessDataItemUsingGET_failure,
  readFitnessDataItemUsingGET_success,
  readFitnessDataListUsingGET,
  readFitnessDataListUsingGET_failure,
  readFitnessDataListUsingGET_success,

} from './action';
import {showNotification} from '../notification/action';
import {DataAccessService, FitnessDataItem, FitnessDataListElement} from 'build/generated';
import {Level, NotificationData} from '../notification/reducer';

@Injectable()
export class Effects {

  constructor(
    private action$: Actions,
    private logger: NGXLogger,
    private dataAccessService: DataAccessService,
  ) {
  }

  // noinspection JSUnusedGlobalSymbols
  readFitnessDataListUsingGET$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readFitnessDataListUsingGET),
      tap((action) => {
        this.logger.info('readFitDataSourcesUsingGET...');
        this.logger.debug('<readFitDataSourcesUsingGET>', JSON.stringify(action));
      }),
      mergeMap((action: {}) => {
        return this.dataAccessService.readFitnessDataList().pipe(
          map((fitDataSources: Array<FitnessDataListElement>) => {
            return readFitnessDataListUsingGET_success({
              payload: fitDataSources,
            });
          }),
          catchError((error: any) => {
            return of(readFitnessDataListUsingGET_failure({
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
  readFitnessDataListUsingGET_failure$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readFitnessDataListUsingGET_failure),
      tap((action) => {
        this.logger.debug('<readRandomDataUsingGET_failure>', JSON.stringify(action));
      }),
      map((action: { payload: NotificationData }) => {
        return showNotification({payload: action.payload});
      }),
    );
  });


  // noinspection JSUnusedGlobalSymbols
  readFitnessDataItemUsingGET$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readFitnessDataItemUsingGET),
      tap((action) => {
        this.logger.info('readFitSourceUsingGET...');
        this.logger.debug('<readFitSourceUsingGET>', JSON.stringify(action));
      }),
      mergeMap((action) => {
        return this.dataAccessService.readFitnessDataItem({ id: action.payload }).pipe( // fixme
          map((fitnessDataElement: FitnessDataItem) => {
            return readFitnessDataItemUsingGET_success({
              payload: fitnessDataElement,
            });
          }),
          catchError((error: any) => {
            return of(readFitnessDataItemUsingGET_failure({
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

}
