import {Action, createReducer, on} from '@ngrx/store';
import {FitnessDataItem, FitnessDataListElement, FitnessDataTimeseries} from 'build/generated';
import * as fromActions from './action';
import {LoggerHolder} from '../../shared/logger-holder';
import {readFitnessDataTimeseriesUsingGET_success} from './action';


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
  fitnessTimeseries: {
    isLoading: boolean
    isError: boolean
    beginInMillisecond: number | undefined
    endInMillisecond: number | undefined
    data: FitnessDataTimeseries | undefined,
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
  fitnessTimeseries: {
    isLoading: false,
    isError: false,
    beginInMillisecond: undefined,
    endInMillisecond: undefined,
    data: undefined,
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

  on(fromActions.setFitnessTimeseriesBegin,
    (state: FitnessState, {payload: payload}): FitnessState => {
      LoggerHolder.logger.debug(`<setFitnessTimeseriesBegin> payload: `, JSON.stringify(payload, null, 2));
      return {
        ...state,
        fitnessTimeseries:
          {
            isLoading: false,
            isError: false,
            // keep existing value or override with new value
            beginInMillisecond: payload.beginInMillisecond,
            endInMillisecond: state.fitnessTimeseries.endInMillisecond,
            data: undefined
          },
      };
    },
  ),

  on(fromActions.setFitnessTimeseriesEnd,
    (state: FitnessState, {payload: payload}): FitnessState => {
      LoggerHolder.logger.debug(`<setFitnessTimeseriesEnd> payload: `, JSON.stringify(payload, null, 2));
      return {
        ...state,
        fitnessTimeseries:
          {
            isLoading: false,
            isError: false,
            // keep existing value or override with new value
            beginInMillisecond: state.fitnessTimeseries.beginInMillisecond,
            endInMillisecond: payload.endInMillisecond,
            data: undefined
          },
      };
    },
  ),


  on(fromActions.readFitnessDataTimeseriesUsingGET_success,
    (state: FitnessState, {payload: payload}): FitnessState => {
      LoggerHolder.logger.debug(`<readFitnessDataTimeseriesUsingGET_success> payload: `, JSON.stringify(payload, null, 2));
      return {
        ...state,
        fitnessTimeseries:
          {
            isLoading: false,
            isError: false,
            // keep existing value or override with new value
            beginInMillisecond: state.fitnessTimeseries.beginInMillisecond,
            endInMillisecond: state.fitnessTimeseries.endInMillisecond,
            data: payload
          },
      };
    },
  ),


);

export function reducer(state: FitnessState | undefined, action: Action): FitnessState {
  return featureReducer(state, action);
}
