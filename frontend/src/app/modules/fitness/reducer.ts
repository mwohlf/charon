import {Action, createReducer, on} from '@ngrx/store';
import {FitnessDataItem, FitnessDataListElement} from 'build/generated';
import * as fromActions from './action';
import {LoggerHolder} from '../../shared/logger-holder';


export interface FitnessState {
  fitnessDataList: {
    isLoading: boolean
    isError: boolean
    fitnessDataListElements: Array<FitnessDataListElement> | undefined
  };
  fitnessItem: {
    isLoading: boolean
    isError: boolean
    fitnessDataItem: FitnessDataItem | undefined
  };
  fitnessTimeSeries: {
    isLoading: boolean
    isError: boolean
    beginInMillisecond: number | undefined
    endInMillisecond: number | undefined
  };
}

const initialState: FitnessState = {
  fitnessDataList: {
    isLoading: false,
    isError: false,
    fitnessDataListElements: undefined,
  },
  fitnessItem: {
    isLoading: false,
    isError: false,
    fitnessDataItem: undefined,
  },
  fitnessTimeSeries: {
    isLoading: false,
    isError: false,
    beginInMillisecond: undefined,
    endInMillisecond: undefined,
  },
};

const featureReducer = createReducer(
  initialState,

  on(fromActions.readFitnessDataListUsingGET,
    (state: FitnessState): FitnessState => {
      return {
        ...state,
        fitnessDataList:
          {
            isLoading: true,
            isError: false,
            fitnessDataListElements: undefined,
          },
      };
    },
  ),

  on(fromActions.readFitnessDataListUsingGET_success,
    (state: FitnessState, {payload: payload}): FitnessState => {
      LoggerHolder.logger.debug(`<readFitDataSourcesUsingGET_success> payload: `, JSON.stringify(payload, null, 2));
      return {
        ...state,
        fitnessDataList:
          {
            isLoading: false,
            isError: false,
            fitnessDataListElements: payload,
          },
      };
    },
  ),

  on(fromActions.readFitnessDataListUsingGET_failure,
    (state: FitnessState, {payload: error}): FitnessState => {
      LoggerHolder.logger.debug(`<readFitDataSourcesUsingGET_failure> error: ${error}, state: `, JSON.stringify(state, null, 2));
      return {
        ...state,
        fitnessDataList:
          {
            isLoading: false,
            isError: true,
            fitnessDataListElements: undefined,
          },
      };
    },
  ),

  on(fromActions.readFitnessDataItemUsingGET,
    (state: FitnessState): FitnessState => {
      return {
        ...state,
        fitnessItem:
          {
            isLoading: true,
            isError: false,
            fitnessDataItem: undefined,
          },
      };
    },
  ),

  on(fromActions.readFitnessDataItemUsingGET_success,
    (state: FitnessState, {payload: payload}): FitnessState => {
      LoggerHolder.logger.debug(`<readFitnessDataItemUsingGET_success> payload: `, JSON.stringify(payload, null, 2));
      return {
        ...state,
        fitnessItem:
          {
            isLoading: false,
            isError: false,
            fitnessDataItem: payload,
          },
      };
    },
  ),

  on(fromActions.readFitnessDataItemUsingGET_failure,
    (state: FitnessState, {payload: error}): FitnessState => {
      LoggerHolder.logger.debug(`<readFitnessDataItemUsingGET_failure> error: ${error}, state: `, JSON.stringify(state, null, 2));
      return {
        ...state,
        fitnessItem:
          {
            isLoading: false,
            isError: true,
            fitnessDataItem: undefined,
          },
      };
    },
  ),

  on(fromActions.setFitnessTimeSeriesBegin,
    (state: FitnessState, {payload: payload}): FitnessState => {
      LoggerHolder.logger.debug(`<setFitnessTimeSeriesBegin> payload: ${payload}, state: `, JSON.stringify(state, null, 2));
      return {
        ...state,
        fitnessTimeSeries:
          {
            isLoading: false,
            isError: false,
            // keep existing value or override with new value
            beginInMillisecond: payload.beginInMillisecond,
            endInMillisecond: state.fitnessTimeSeries.endInMillisecond,
          },
      };
    },
  ),

  on(fromActions.setFitnessTimeSeriesEnd,
    (state: FitnessState, {payload: payload}): FitnessState => {
      LoggerHolder.logger.debug(`<setFitnessTimeSeriesEnd> payload: ${payload}, state: `, JSON.stringify(state, null, 2));
      return {
        ...state,
        fitnessTimeSeries:
          {
            isLoading: false,
            isError: false,
            // keep existing value or override with new value
            beginInMillisecond: state.fitnessTimeSeries.beginInMillisecond,
            endInMillisecond: payload.endInMillisecond,
          },
      };
    },
  ),



);

export function reducer(state: FitnessState | undefined, action: Action): FitnessState {
  return featureReducer(state, action);
}
