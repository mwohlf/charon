import {Action, createReducer, on} from '@ngrx/store';
import { ConfigurationDetails } from 'build/generated';
import * as fromActions from './action';
import {LoggerHolder} from '../../shared/logger-holder';


export interface ConfigState {
  isLoading: boolean;
  isError: boolean;
  config: ConfigurationDetails | undefined;
}

const initialState: ConfigState = {
  isLoading: false,
  isError: false,
  config: undefined,
};

const featureReducer = createReducer(
  initialState,

  on(fromActions.readConfigurationDetailsUsingGET,
    (state: ConfigState) => {
      return {
        isLoading: true,
        isError: false,
        config: undefined,
      };
    },
  ),

  on(fromActions.readConfigurationDetailsUsingGET_success,
    (state: ConfigState, {payload: payload}) => {
      return {
        isLoading: false,
        isError: false,
        config: payload,
      };
    },
  ),

  on(fromActions.readConfigurationDetailsUsingGET_failure,
    (state: ConfigState, {payload: error}) => {
      LoggerHolder.logger.debug(`<readConfigurationDetailsUsingGET_failure> error: ${error}, state: `, JSON.stringify(state));
      return {
        isLoading: false,
        isError: true,
        config: undefined,
      };
    },
  ),

);

export function reducer(state: ConfigState | undefined, action: Action): ConfigState {
  return featureReducer(state, action);
}
