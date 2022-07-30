import {createAction, props} from '@ngrx/store';
import { ConfigurationDetails } from 'build/generated';
import {ErrorDetails} from '../error/action';

// this is a handle for action chaining,
// whatever needs to be done after the config is loaded can happen afterwards...
export const CONFIG_INITIALIZED = "@modules/config/readConfigurationDetailsUsingGET_success";

export const readConfigurationDetailsUsingGET = createAction(
  "@modules/config/readConfigurationDetailsUsingGET",
);

export const readConfigurationDetailsUsingGET_success = createAction(
  CONFIG_INITIALIZED,
  props<{ payload: ConfigurationDetails }>()
);

export const readConfigurationDetailsUsingGET_failure = createAction(
  '@modules/config/readConfigurationDetailsUsingGET_failure',
  props<{ payload: ErrorDetails }>()
);
