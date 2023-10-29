import {createAction, props} from '@ngrx/store';
import {ProtectedData} from 'build/generated';
import {NotificationData} from '../notification/reducer';
import {dataFeature} from '../../shared/const';

const GROUP: string = '@app/' + dataFeature;


export const readProtectedDataUsingGET = createAction(
  `${GROUP}/readProtectedDataUsingGET`,
);

export const readProtectedDataUsingGET_success = createAction(
  `${GROUP}/readProtectedDataUsingGET_success`,
  props<{ payload: ProtectedData }>(),
);

export const readProtectedDataUsingGET_failure = createAction(
  `${GROUP}/readProtectedDataUsingGET_failure`,
  props<{ payload: NotificationData }>(),
);

