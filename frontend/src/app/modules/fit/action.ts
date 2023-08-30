import {createAction, props} from '@ngrx/store';
import {NotificationData} from '../notification/reducer';
import {fitFeature} from '../../shared/const';
import {FitSource} from './reducer';
import {FitnessDataItem, FitnessDataListElement} from 'build/generated';

const GROUP: string = '@app/' + fitFeature;


export const readFitnessDataListUsingGET = createAction(
  `${GROUP}/readFitnessDataListUsingGET`,
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
  props<{ payload: string }>(),
);


export const readFitnessDataItemUsingGET_success = createAction(
  `${GROUP}/readFitnessDataItemUsingGET_success`,
  props<{ payload: FitnessDataItem }>(),
);

export const readFitnessDataItemUsingGET_failure = createAction(
  `${GROUP}/readFitnessDataItemUsingGET_failure`,
  props<{ payload: NotificationData }>(),
);
