import {createAction, props} from '@ngrx/store';
import {ConfigurationDetails} from 'build/generated';
import {NotificationData} from '../notification/reducer';
import {configFeature} from '../../shared/const';

const GROUP: string = '@app/' + configFeature;

// this is a handle for action chaining,
// whatever needs to be done after the config is loaded can happen afterward...
export const CONFIG_INITIALIZED = `${GROUP}/readConfigurationDetailsUsingGET_success`;


export const readConfigurationDetailsUsingGET = createAction(
  `${GROUP}/readConfigurationDetailsUsingGET`,
);

export const readConfigurationDetailsUsingGET_success = createAction(
  CONFIG_INITIALIZED,
  props<{ payload: ConfigurationDetails }>(),
);

export const readConfigurationDetailsUsingGET_failure = createAction(
  `${GROUP}/readConfigurationDetailsUsingGET_failure`,
  props<{ payload: NotificationData }>(),
);
