import {createFeatureSelector, createSelector} from '@ngrx/store';
import {featureKey, NotificationData, NotificationQueue} from './reducer';

export const selectNotificationFeature = createFeatureSelector<NotificationQueue>(featureKey);

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

