import {Action, createReducer, on} from '@ngrx/store';
import {ProtectedData} from 'build/generated';
import * as fromActions from './action';
import {LoggerHolder} from '../../shared/logger-holder';


export interface DataState {
  isLoading: boolean;
  isError: boolean;
  protectedData: ProtectedData | undefined;
}

const initialState: DataState = {
  isLoading: false,
  isError: false,
  protectedData: undefined,
};

const featureReducer = createReducer(
  initialState,

  on(fromActions.readProtectedDataUsingGET,
    (state: DataState): DataState => {
      return {
        isLoading: true,
        isError: false,
        protectedData: undefined,
      };
    },
  ),

  on(fromActions.readProtectedDataUsingGET_success,
    (state: DataState, {payload: payload}): DataState => {
      return {
        isLoading: false,
        isError: false,
        protectedData: payload,
      };
    },
  ),

  on(fromActions.readProtectedDataUsingGET_failure,
    (state: DataState, {payload: error}): DataState => {
      return {
        isLoading: false,
        isError: true,
        protectedData: undefined,
      };
    },
  ),
);

export function reducer(state: DataState | undefined, action: Action): DataState {
  return featureReducer(state, action);
}
