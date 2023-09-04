import {createAction, props} from '@ngrx/store';
import {NotificationData} from '../notification/reducer';
import {fitFeature} from '../../shared/const';
import {FitnessDataItem, FitnessDataListElement} from 'build/generated';

const GROUP: string = '@app/' + fitFeature;


export const readFitnessDataListUsingGET = createAction(
  `${GROUP}/readFitnessDataListUsingGET`,
  props<{ payload: { userId: string } }>(),
);

export const readFitnessDataListUsingGET_success = createAction(
  `${GROUP}/readFitnessDataListUsingGET_success`,
  props<{ payload: Array<FitnessDataListElement> }>(),
);

export const readFitnessDataListUsingGET_failure = createAction(
  `${GROUP}/readFitnessDataListUsingGET_failure`,
  props<{ payload: NotificationData }>(),
);

export const readFitnessDataItemUsingGET = createAction(
  `${GROUP}/readFitnessDataItemUsingGET`,
  props<{ payload: { userId: string, dataSourceId: string } }>(),
);


export const readFitnessDataItemUsingGET_success = createAction(
  `${GROUP}/readFitnessDataItemUsingGET_success`,
  props<{ payload: FitnessDataItem }>(),
);

export const readFitnessDataItemUsingGET_failure = createAction(
  `${GROUP}/readFitnessDataItemUsingGET_failure`,
  props<{ payload: NotificationData }>(),
);


export const setFitnessTimeSeriesBegin = createAction(
  `${GROUP}/setFitnessTimeSeriesBegin`,
  props<{
    payload: {
      beginInMillisecond: number | undefined,
    }
  }>(),
);

export const setFitnessTimeSeriesEnd = createAction(
  `${GROUP}/setFitnessTimeSeriesEnd`,
  props<{
    payload: {
      endInMillisecond: number | undefined,
    }
  }>(),
);

export const updateTimeSeries = createAction(
  `${GROUP}/updateTimeSeries`,
);

export const readFitnessDataTimeSeriesUsingGET = createAction(
  `${GROUP}/readFitnessDataTimeSeriesUsingGET`,
  props<{
    payload: {
      userId: string,
      beginInMillisecond: number,
      endInMillisecond: number,
      dataSourceId: string,
    }
  }>(),
);

export const readFitnessDataTimeSeriesUsingGET_success = createAction(
  `${GROUP}/readFitnessDataTimeSeriesUsingGET_success`,
  props<{
    payload: any
  }>(),
);

export const readFitnessDataTimeSeriesUsingGET_failure = createAction(
  `${GROUP}/readFitnessDataTimeSeriesUsingGET_failure`,
  props<{ payload: NotificationData }>(),
);
