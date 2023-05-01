import {createAction, props} from '@ngrx/store';
import {NotificationData} from './reducer';
import {notificationFeature} from '../../shared/const';

const GROUP: string = '@app/' + notificationFeature;

export const showNotification = createAction(
  `${GROUP}/showNotification`,
  props<{ payload: NotificationData }>(),
);

export const confirmNotification = createAction(
  `${GROUP}/confirmNotification`,
);


