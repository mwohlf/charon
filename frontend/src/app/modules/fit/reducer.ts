import {Action, createReducer, on} from '@ngrx/store';
import {FitnessDataListElement} from 'build/generated';
import * as fromActions from './action';
import {LoggerHolder} from '../../shared/logger-holder';


export interface FitState {
  dataSources: {
    isLoading: boolean;
    isError: boolean;
    fitDataSources: Array<FitnessDataListElement> | undefined;
  };
}

export interface FitSource {
}


const initialState: FitState = {
  dataSources: {
    isLoading: false,
    isError: false,
    fitDataSources: undefined,
  },
};

const featureReducer = createReducer(
  initialState,

  on(fromActions.readFitnessDataListUsingGET,
    (state: FitState) => {
      return {
        ...state,
        dataSources:
          {
            isLoading: true,
            isError: false,
            fitDataSources: undefined,
          },
      };
    },
  ),

  on(fromActions.readFitnessDataListUsingGET_success,
    (state: FitState, {payload: payload}) => {
      LoggerHolder.logger.debug(`<readFitDataSourcesUsingGET_success> payload: `, JSON.stringify(payload));
      return {
        ...state,
        dataSources:
          {
            isLoading: false,
            isError: false,
            fitDataSources: payload,
          }
      };
    },
  ),

  on(fromActions.readFitnessDataListUsingGET_failure,
    (state: FitState, {payload: error}) => {
      LoggerHolder.logger.debug(`<readFitDataSourcesUsingGET_failure> error: ${error}, state: `, JSON.stringify(state));
      return {
        ...state,
        dataSources:
          {
            isLoading: false,
            isError: true,
            fitDataSources: undefined,
          }
      };
    },
  ),
);

export function reducer(state: FitState | undefined, action: Action): FitState {
  return featureReducer(state, action);
}
