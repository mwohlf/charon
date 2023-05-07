import {ActionReducerMap, createFeatureSelector} from '@ngrx/store';
import * as fromRouterStore from '@ngrx/router-store';
import {BaseRouterStoreState} from '@ngrx/router-store';
import {AppState} from '../../app-shell.module';
import {routerReducer} from '@ngrx/router-store';

export const featureKey = 'routingFeature';

export const selectRouter = createFeatureSelector<fromRouterStore.RouterReducerState<BaseRouterStoreState>>(featureKey);

export const {
  selectCurrentRoute,   // select the current route
  selectFragment,       // select the current route fragment
  selectQueryParams,    // select the current route query params
  selectQueryParam,     // factory function to select a query param
  selectRouteParams,    // select the current route params
  selectRouteParam,     // factory function to select a route param
  selectRouteData,      // select the current route data
  selectUrl,            // select the current url
} = fromRouterStore.getRouterSelectors(selectRouter);


export const reducer: ActionReducerMap<AppState> = {
  routingFeature: routerReducer
};
