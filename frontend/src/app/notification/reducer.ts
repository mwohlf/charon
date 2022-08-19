import {Action, createReducer, on} from '@ngrx/store';
import * as fromActions from './action';
import * as _ from 'lodash';

export const featureKey = 'notificationFeature';

export interface NotificationData {
  title: string;
  message: string;
  details: string;
}

export interface NotificationQueue {
  notificationQueue: Array<NotificationData>;
}

const initialState: NotificationQueue = {
  notificationQueue: Array<NotificationData>(),
};

const featureReducer = createReducer(
  initialState,

  on(fromActions.showNotification,
    (currentState, {payload: payload}) => {
      const notificationQueue = _.cloneDeep(currentState.notificationQueue);
      notificationQueue.push(payload)
      return {
        notificationQueue: notificationQueue,
      };
    },
  ),

  on(fromActions.confirmNotification,
    (currentState) => {
      const notificationQueue = currentState.notificationQueue.slice(1)
      return {
        notificationQueue: notificationQueue,
      };
    },
  ),

);

export function reducer(state: NotificationQueue | undefined, action: Action): NotificationQueue {
  return featureReducer(state, action);
}
