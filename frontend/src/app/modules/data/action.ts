import {createAction, props} from '@ngrx/store';
import {RandomData} from 'build/generated';
import {NotificationData} from '../notification/reducer';
import {dataFeature} from '../../shared/const';

const GROUP: string = '@app/' + dataFeature;

export const readRandomDataUsingGET = createAction(
  `${GROUP}/readRandomDataUsingGET`,
);

export const readRandomDataUsingGET_success = createAction(
  `${GROUP}/readRandomDataUsingGET_success`,
  props<{ payload: RandomData }>(),
);

export const readRandomDataUsingGET_failure = createAction(
  `${GROUP}/readRandomDataUsingGET_failure`,
  props<{ payload: NotificationData }>(),
);
