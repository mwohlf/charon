import {Action, createReducer, on} from '@ngrx/store';
import { ConfigurationDetails } from 'build/generated';
import * as fromActions from './action';
import {ErrorDetails} from '../error/action';

export const featureKey = 'configFeature';

export interface ConfigState {
  isLoading: boolean;
  config: ConfigurationDetails | undefined;
  error: ErrorDetails | undefined;
}

const initialState: ConfigState = {
  isLoading: false,
  config: undefined,
  error: undefined,
};

const featureReducer = createReducer(
  initialState,

  on(fromActions.readConfigurationDetailsUsingGET,
    (state: ConfigState) => {
      return {
        isLoading: true,
        config: undefined,
        error: undefined,
      };
    },
  ),

  on(fromActions.readConfigurationDetailsUsingGET_success,
    (state: ConfigState, {payload: payload}) => {
      return {
        isLoading: false,
        config: payload,
        error: undefined,
      };
    },
  ),

  on(fromActions.readConfigurationDetailsUsingGET_failure,
    (state: ConfigState, {payload: error}) => {
      return {
        isLoading: false,
        config: undefined,
        error: error,
      };
    },
  ),

);

export function reducer(state: ConfigState | undefined, action: Action): ConfigState {
  return featureReducer(state, action);
}
