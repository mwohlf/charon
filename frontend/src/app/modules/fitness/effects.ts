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
  readFitnessDataTimeseriesUsingGET,
  readFitnessDataTimeseriesUsingGET_failure,
  readFitnessDataTimeseriesUsingGET_success,
  setFitnessTimeseriesBegin,
  setFitnessTimeseriesEnd,
  updateTimeseries,
} from './action';
import {showNotification} from '../notification/action';
import {
  FitnessDataItem,
  FitnessDataListElement,
  FitnessDataTimeseries,
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

  setFitnessTimeseriesBegin$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(setFitnessTimeseriesBegin),
      map((action) => {
        return updateTimeseries();
      }),
    );
  });

  setFitnessTimeseriesEnd$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(setFitnessTimeseriesEnd),
      map((action) => {
        return updateTimeseries();
      }),
    );
  });

  updateTimeseries$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(updateTimeseries),
      // picking a value from the state store
      withLatestFrom(this.store.select(selectFitFeature)),
      distinct(),
      filter(([action, storeState]) => {
        // noinspection UnnecessaryLocalVariableJS
        const result: boolean = !!storeState.fitnessTimeseries.beginInMillisecond
          && !!storeState.fitnessTimeseries.endInMillisecond
          && !!storeState.fitnessItem.fitnessDataItem?.id;
        return result
      }),
      map(([action, storeState]) => {
        this.logger.info('requesting timeseries for state: ', JSON.stringify(storeState) );
        return readFitnessDataTimeseriesUsingGET({
          payload: {
            userId: 'me',
            beginInMillisecond: storeState.fitnessTimeseries.beginInMillisecond!!,
            endInMillisecond: storeState.fitnessTimeseries.endInMillisecond!!,
            dataSourceId: storeState.fitnessItem.fitnessDataItem!!.id,
          },
        });
      }),
    ); // end pipe
  });

  readFitnessDataTimeseriesUsingGET$ : Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readFitnessDataTimeseriesUsingGET),
      tap((action) => {
        this.logger.debug('<readFitnessDataTimeseriesUsingGET$>', JSON.stringify(action));
      }),
      mergeMap((action) => {
        const dataSetId: string = ''
          + action.payload.beginInMillisecond + "000000"
          + '-'
          + action.payload.endInMillisecond + "000000";
        this.logger.debug('<readFitnessDataTimeseriesUsingGET$> dataSetId:', dataSetId);
        return this.fitnessStoreService
          .readFitnessDataTimeseries({
            userId: action.payload.userId,
            dataSourceId: action.payload.dataSourceId,
            dataSetId: dataSetId,
          })
          .pipe( // fixme
            map((result: FitnessDataTimeseries) => {
              return readFitnessDataTimeseriesUsingGET_success({
                payload: result,
              });
            }),
            catchError((error: any) => {
              return of(readFitnessDataTimeseriesUsingGET_failure({
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

  // noinspection JSUnusedGlobalSymbols
  readFitnessDataTimeseriesUsingGET_success$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(readFitnessDataTimeseriesUsingGET_success),
      tap((action) => {
        this.logger.debug('<readFitnessDataTimeseriesUsingGET_success>', JSON.stringify(action));
      }),
    );
  }, {dispatch: false});

// , {dispatch: false});
}
