import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Action, Store} from '@ngrx/store';
import {distinct, Observable, of, withLatestFrom} from 'rxjs';
import {catchError, filter, map, mergeMap, tap} from 'rxjs/operators';

import {NGXLogger} from 'ngx-logger';

import {
  readFitnessDataItemUsingGET,
  readFitnessDataItemUsingGET_failure,
  readFitnessDataItemUsingGET_success,
  readFitnessDataListUsingGET,
  readFitnessDataListUsingGET_failure,
  readFitnessDataListUsingGET_success,
  readFitnessDataTimeSeriesUsingGET,
  readFitnessDataTimeSeriesUsingGET_failure,
  readFitnessDataTimeSeriesUsingGET_success,
  setFitnessTimeSeriesBegin,
  setFitnessTimeSeriesEnd,
  updateTimeSeries,
} from './action';
import {showNotification} from '../notification/action';
import {
  FitnessDataItem,
  FitnessDataListElement,
  FitnessStoreService,
} from 'build/generated';
import {Level, NotificationData} from '../notification/reducer';
import {selectFitFeature} from './selector';
import {AppState} from '../../app-shell.module';

@Injectable()
export class Effects {

  constructor(
    private action$: Actions,
    private logger: NGXLogger,
    private fitnessStoreService: FitnessStoreService,
    private store: Store<AppState>,
  ) {
  }

  // noinspection JSUnusedGlobalSymbols
  readFitnessDataListUsingGET$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readFitnessDataListUsingGET),
      tap((action) => {
        this.logger.info('readFitnessDataListUsingGET...');
        this.logger.debug('<readFitnessDataListUsingGET>', JSON.stringify(action, null, 2));
      }),
      mergeMap((action) => {
        return this.fitnessStoreService.readFitnessDataList({userId: action.payload.userId}).pipe(
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
      tap((action): void => {
        this.logger.debug('<readFitnessDataListUsingGET_failure>', JSON.stringify(action));
      }),
      map((action: {
        payload: NotificationData
      }) => {
        return showNotification({payload: action.payload});
      }),
    );
  });


  // noinspection JSUnusedGlobalSymbols
  readFitnessDataItemUsingGET$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readFitnessDataItemUsingGET),
      tap((action): void => {
        this.logger.info('readFitnessDataItemUsingGET...');
        this.logger.debug('<readFitnessDataItemUsingGET>', JSON.stringify(action));
      }),
      mergeMap((action) => {
        return this.fitnessStoreService
          .readFitnessDataItem({
            userId: action.payload.userId,
            dataSourceId: action.payload.dataSourceId,
          })
          .pipe( // fixme
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

  // forward as error action...
  // noinspection JSUnusedGlobalSymbols
  readFitnessDataItemUsingGET_failure$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readFitnessDataItemUsingGET_failure),
      tap((action) => {
        this.logger.debug('<readFitnessDataItemUsingGET_failure>', JSON.stringify(action));
      }),
      map((action: {
        payload: NotificationData
      }) => {
        return showNotification({payload: action.payload});
      }),
    );
  });

  setFitnessTimeSeriesBegin$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(setFitnessTimeSeriesBegin),
      map((action) => {
        return updateTimeSeries();
      }),
    );
  });

  setFitnessTimeSeriesEnd$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(setFitnessTimeSeriesEnd),
      map((action) => {
        return updateTimeSeries();
      }),
    );
  });

  updateTimeseries$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(updateTimeSeries),
      withLatestFrom(this.store.select(selectFitFeature)),
      distinct(),
      filter(([action, storeState]) => {
        var result = !!storeState.fitnessTimeSeries.beginInMillisecond
          && !!storeState.fitnessTimeSeries.endInMillisecond
          && !!storeState.fitnessItem.fitnessDataItem?.id;
        this.logger.info('result: ', result );
        return result
      }),
      map(([action, storeState]) => {
        return readFitnessDataTimeSeriesUsingGET({
          payload: {
            userId: 'me',
            beginInMillisecond: storeState.fitnessTimeSeries.beginInMillisecond!!,
            endInMillisecond: storeState.fitnessTimeSeries.endInMillisecond!!,
            dataSourceId: storeState.fitnessItem.fitnessDataItem!!.id,
          },
        });
      }),
    ); // end pipe
  });

  readFitnessDataTimeSeriesUsingGET$ : Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readFitnessDataTimeSeriesUsingGET),
      tap((action) => {
        this.logger.debug('<readFitnessDataTimeSeriesUsingGET$>', JSON.stringify(action));
      }),
      mergeMap((action) => {
        const dataSetId: string = ''
          + action.payload.beginInMillisecond + "000000"
          + '-'
          + action.payload.endInMillisecond + "000000";
        this.logger.debug('<readFitnessDataTimeSeriesUsingGET$> dataSetId:', dataSetId);
        return this.fitnessStoreService
          .readFitnessDataSet({
            userId: action.payload.userId,
            dataSourceId: action.payload.dataSourceId,
            dataSetId: dataSetId,
          })
          .pipe( // fixme
            map((result: any) => {
              return readFitnessDataTimeSeriesUsingGET_success({
                payload: result,
              });
            }),
            catchError((error: any) => {
              return of(readFitnessDataTimeSeriesUsingGET_failure({
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
    ); // end pipe
  });

// , {dispatch: false});
}
