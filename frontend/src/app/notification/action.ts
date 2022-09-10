import {createAction, props} from '@ngrx/store';
import {NotificationData} from './reducer';
import {notificationFeature} from '../const';

const GROUP = '@app/' + notificationFeature;

export const showNotification = createAction(
  GROUP + '/showNotification',
  props<{ payload: NotificationData }>(),
);

export const confirmNotification = createAction(
  GROUP + '/confirmNotification',
);


