import {Action, createReducer, on} from '@ngrx/store';
import {RandomData} from 'build/generated';
import * as fromActions from './action';


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
    (state: DataState) => {
      return {
        isLoading: true,
        isError: false,
        randomData: undefined,
      };
    },
  ),

  on(fromActions.readRandomDataUsingGET_success,
    (state: DataState, {payload: payload}) => {
      return {
        isLoading: false,
        isError: false,
        randomData: payload,
      };
    },
  ),

  on(fromActions.readRandomDataUsingGET_failure,
    (state: DataState, {payload: error}) => {
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
