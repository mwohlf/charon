import {createAction, props} from '@ngrx/store';
import {NotificationData} from './reducer';

const GROUP = '@app/notification';


export const showNotification = createAction(
  GROUP + '/showNotification',
  props<{ payload: NotificationData }>(),
);

export const confirmNotification = createAction(
  GROUP + '/confirmNotification',
);


