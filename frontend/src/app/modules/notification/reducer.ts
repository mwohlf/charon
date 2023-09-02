import {Action, createReducer, on} from '@ngrx/store';
import * as fromActions from './action';
import * as _ from 'lodash-es';


export enum Level {
  Error,
  Warning,
  Info,
  Debug,
}

export interface NotificationData {
  level: Level;
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
    (currentState, {payload: payload}): NotificationQueue => {
    // we need to clone because our state is immutable
      const notificationQueue = _.cloneDeep(currentState.notificationQueue);
      // if we have an incoming error we override any other notification
      if (payload.level === Level.Error) {
        notificationQueue.length = 0;
      }
      notificationQueue.push(payload)
      return {
        notificationQueue: notificationQueue,
      };
    },
  ),

  on(fromActions.confirmNotification,
    (currentState): NotificationQueue => {
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
