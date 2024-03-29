import {AppComponent} from './app.component';
import {RoutingModule} from './modules/routing/routing.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {BrowserModule} from '@angular/platform-browser';
import {LayoutModule} from '@angular/cdk/layout';
import {ErrorHandler, NgModule} from '@angular/core';
import {ApiModule, Configuration} from '../../build/generated';
import {Action, ActionReducer, MetaReducer, StoreModule} from '@ngrx/store';
import * as fromConfig from './modules/config/effects';
import * as fromOAuth from './modules/oauth/effects';
import * as fromTheme from './modules/view/effects';
import * as fromData from './modules/data/effects';
import * as fromFit from './modules/fitness/effects';
import {AppThemeModule} from './app-theme.module';
import {FooterComponent} from './components/footer/footer.component';
import {MenuComponent} from './components/menu/menu.component';
import {StoreRouterConnectingModule} from '@ngrx/router-store';
import {environment} from '../environments/environment';
import {StoreDevtoolsModule} from '@ngrx/store-devtools';
import * as fromRoutingReducer from './modules/routing/reducer';
import {ConfigModule} from './modules/config/config.module';
import {EffectsModule, EffectsRootModule} from '@ngrx/effects';
import {LoggerModule, NgxLoggerLevel} from 'ngx-logger';
import {OAuthModule} from './modules/oauth/oauth.module';
import {HttpClientModule} from '@angular/common/http';
import {MAT_SNACK_BAR_DEFAULT_OPTIONS} from '@angular/material/snack-bar';
import {NotificationModule} from './modules/notification/notification.module';
import {ViewModule} from './components/theme-picker/view.module';
import {MatSidenavModule} from '@angular/material/sidenav';
import {HeaderComponent} from './components/header/header.component';
import {GlobalErrorHandler} from './shared/error-handler';
import {PageComponent} from './components/page/page.component';
import {DataModule} from './modules/data/data.module';
import {FitnessModule} from './modules/fitness/fitness.module';
import {FitSourcesGrid} from './components/fit-sources-grid/fit-sources-grid';
import {FitDataChart} from './components/fit-data-chart/fit-data-chart';
import {AngularResizeEventModule} from 'angular-resize-event';


export interface AppState {
}


export function logging(reducer: ActionReducer<AppState>): ActionReducer<AppState> {
  return function(state: AppState | undefined, action: Action) {
    // TODO log here
    return reducer(state, action);
  };
}

export const appMetaReducers: MetaReducer[] = !environment.production
  ? [logging]
  : [];

const prefersReducedMotion =
  typeof matchMedia === 'function' ? matchMedia('(prefers-reduced-motion)').matches : false;

@NgModule({
  bootstrap: [AppComponent],
  declarations: [
    AppComponent,
  ],
  imports: [
    AngularResizeEventModule,
    AppThemeModule,
    BrowserAnimationsModule.withConfig({disableAnimations: prefersReducedMotion}),
    BrowserModule,
    ConfigModule,
    DataModule,
    EffectsRootModule,
    FitnessModule,
    HttpClientModule,
    LayoutModule,
    MatSidenavModule,
    NotificationModule,
    OAuthModule,
    RoutingModule,
    ViewModule,
    ApiModule.forRoot(() => new Configuration({
      basePath: environment.apiBasePath,
    })),
    StoreModule.forRoot(fromRoutingReducer.reducer, {
      metaReducers: appMetaReducers,
      runtimeChecks: {
        strictStateImmutability: true,
        strictActionImmutability: true,
        strictStateSerializability: true,
        strictActionSerializability: true,
        strictActionWithinNgZone: true,
        strictActionTypeUniqueness: true,
      },
    }),
    StoreRouterConnectingModule.forRoot({
      stateKey: fromRoutingReducer.featureKey,
    }),
    StoreDevtoolsModule.instrument({
      name: 'charon',
      logOnly: environment.production,
    }),
    EffectsModule.forRoot([
      fromConfig.Effects,
      fromOAuth.Effects,
      fromTheme.Effects,
      fromData.Effects,
      fromFit.Effects,
    ]),
    LoggerModule.forRoot({
      serverLoggingUrl: '/api/logs', // fix this after config
      level: NgxLoggerLevel.DEBUG,
      serverLogLevel: NgxLoggerLevel.ERROR,
    }),
    HeaderComponent,
    FooterComponent,
    MenuComponent,
    PageComponent,
    FitSourcesGrid,
    FitDataChart,
  ],
  providers: [
    {
      provide: MAT_SNACK_BAR_DEFAULT_OPTIONS,
      useValue: {duration: 2500},
    },
    {
      provide: ErrorHandler,
      useClass: GlobalErrorHandler,
    },
  ],
})
export class AppShellModule {
}
