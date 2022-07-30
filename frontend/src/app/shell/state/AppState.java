import * as fromRouterStore from '@ngrx/router-store';
import {routerReducer, RouterReducerState} from '@ngrx/router-store';
import {BaseRouterStoreState} from '@ngrx/router-store/src/serializers/base';
import {ActionReducerMap, createFeatureSelector} from '@ngrx/store';

// basic application state, feature states are added by the modules
export interface AppState {
    routerFeature: RouterReducerState<BaseRouterStoreState>;
}

export const appReducer: ActionReducerMap<AppState> = {
    routerFeature: routerReducer,
};

export const selectRouter = createFeatureSelector<AppState, fromRouterStore.RouterReducerState<BaseRouterStoreState>>('routerFeature');

export const {
    selectCurrentRoute,   // select the current route
    selectFragment,       // select the current route fragment
    selectQueryParams,    // select the current route query params
    selectQueryParam,     // factory function to select a query param
    selectRouteParams,    // select the current route params
    selectRouteParam,     // factory function to select a route param
    selectRouteData,      // select the current route data
    selectUrl,            // select the current url
} = fromRouterStore.getSelectors(selectRouter);
