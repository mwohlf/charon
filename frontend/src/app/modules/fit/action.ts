import {createAction, props} from '@ngrx/store';
import {NotificationData} from '../notification/reducer';
import {fitFeature} from '../../shared/const';
import {FitDataSource} from 'build/generated/model/fit-data-source';

const GROUP: string = '@app/' + fitFeature;


export const readFitDataSourcesUsingGET = createAction(
  `${GROUP}/readFitDataSourcesUsingGET`,
);

export const readFitDataSourcesUsingGET_success = createAction(
  `${GROUP}/readFitDataSourcesUsingGET_success`,
  props<{ payload: Array<FitDataSource> }>(),
);

export const readFitDataSourcesUsingGET_failure = createAction(
  `${GROUP}/readFitDataSourcesUsingGET_failure`,
  props<{ payload: NotificationData }>(),
);
