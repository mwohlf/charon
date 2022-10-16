import {createFeatureSelector, createSelector} from '@ngrx/store';
import {NotificationQueue} from './reducer';
import {notificationFeature} from '../../const';

export const selectNotificationFeature = createFeatureSelector<NotificationQueue>(notificationFeature);

// see: https://v11.ngrx.io/guide/store/selectors
export const selectNotificationQueue = createSelector(
  selectNotificationFeature,
  (state: NotificationQueue) => {
    if (state.notificationQueue) {
      return state.notificationQueue;
    } else {
      return [];
    }
  },
);

