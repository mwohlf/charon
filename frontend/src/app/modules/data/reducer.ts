import {Action, createReducer, on} from '@ngrx/store';
import {RandomData} from 'build/generated';
import * as fromActions from './action';
import {LoggerHolder} from '../../shared/logger-holder';


export interface DataState {
  isLoading: boolean;
  isError: boolean;
  randomData: RandomData | undefined;
}

const initialState: DataState = {
  isLoading: false,
  isError: false,
  randomData: undefined,
};

const featureReducer = createReducer(
  initialState,

  on(fromActions.readRandomDataUsingGET,
    (state: DataState): DataState => {
      return {
        isLoading: true,
        isError: false,
        randomData: undefined,
      };
    },
  ),

  on(fromActions.readRandomDataUsingGET_success,
    (state: DataState, {payload: payload}): DataState => {
      return {
        isLoading: false,
        isError: false,
        randomData: payload,
      };
    },
  ),

  on(fromActions.readRandomDataUsingGET_failure,
    (state: DataState, {payload: error}): DataState => {
      LoggerHolder.logger.debug(`<readRandomDataUsingGET_failure> error: ${error}, state: `, JSON.stringify(state));
      return {
        isLoading: false,
        isError: true,
        randomData: undefined,
      };
    },
  ),
);

export function reducer(state: DataState | undefined, action: Action): DataState {
  return featureReducer(state, action);
}
