import * as fromActions from './action';
import {Action, createReducer, on} from '@ngrx/store';

export const featureKey = 'headerFeature';

export interface HeaderState {
  isEnabled: boolean;
}

export const initialState: HeaderState = {
  isEnabled: false,
};

const featureReducer = createReducer(
  initialState,
  on(fromActions.enableHeaderFlag,
    (state: HeaderState, {payload: {isEnabled: isEnabled}}) => {
      return {
        ...state, // keep the old state in case we are updating...
        isEnabled: isEnabled,
      };
    },
  ),
  on(fromActions.disableHeaderFlag,
    (state: HeaderState, {payload: {isEnabled: isEnabled}}) => {
      return {
        ...state, // keep the old state in case we are updating...
        isEnabled: isEnabled,
      };
    },
  ),
);


export const reducer = (state: HeaderState | undefined, action: Action) => featureReducer(state, action);
