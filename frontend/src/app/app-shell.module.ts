import {AppComponent} from './app.component';
import {RoutingModule} from './routing/routing.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {BrowserModule} from '@angular/platform-browser';
import {ErrorComponent} from './components/error/error.component';
import {HomeComponent} from './components/home/home.component';
import {LayoutModule} from '@angular/cdk/layout';
import {MainComponent} from './components/main/main.component';
import {NgModule} from '@angular/core';
import {ProtectedComponent} from './components/protected/protected.component';
import {ApiModule, Configuration} from '../../build/generated';
import {Action, ActionReducer, MetaReducer, StoreModule} from '@ngrx/store';
import * as fromConfig from './config/effects';
import * as fromOAuth from './oauth/effects';
import {AppThemeModule} from './app-theme.module';
import {ShellComponent} from './shell/shell.component';
import {FlexLayoutModule} from '@angular/flex-layout';
import {FooterComponent} from './footer/footer.component';
import {MenuComponent} from './menu/menu.component';
import {StoreRouterConnectingModule} from '@ngrx/router-store';
import {environment} from '../environments/environment';
import {HeaderModule} from './header/header.module';
import {StoreDevtoolsModule} from '@ngrx/store-devtools';
import * as fromRouteringReducer from './routing/reducer';
import {ConfigModule} from './config/config.module';
import {EffectsModule, EffectsRootModule} from '@ngrx/effects';
import {LoggerModule, NgxLoggerLevel} from 'ngx-logger';
import {OAuthModule} from './oauth/oauth.module';
import {HttpClientModule} from '@angular/common/http';

export interface AppState {
}


// public logging holder for static contexts
export class LoggerHolder {
  public static logger: any = console;
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

@NgModule({
  declarations: [
    AppComponent,
    ErrorComponent,
    HomeComponent,
    MainComponent,
    ProtectedComponent,
    FooterComponent,
    MenuComponent,
    ShellComponent,
  ],
  imports: [
    ApiModule.forRoot(() => new Configuration({
      basePath: environment.apiBasePath,
    })),
    AppThemeModule,
    OAuthModule,
    BrowserAnimationsModule,
    BrowserModule,
    ConfigModule,
    EffectsRootModule,
    FlexLayoutModule,
    HeaderModule,
    HttpClientModule,
    LayoutModule,
    RoutingModule,
    StoreModule,
    StoreModule.forRoot(fromRouteringReducer.reducer, {
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
    StoreRouterConnectingModule.forRoot({stateKey: fromRouteringReducer.featureKey}),
    StoreDevtoolsModule.instrument({
      name: 'charon',
      logOnly: environment.production,
    }),
    EffectsModule.forRoot([
      fromConfig.Effects,
      fromOAuth.Effects,
    ]),
    EffectsModule.forFeature([]),
    LoggerModule.forRoot({
      serverLoggingUrl: '/api/logs', // fix this after config
      level: NgxLoggerLevel.DEBUG,
      serverLogLevel: NgxLoggerLevel.ERROR,
    }),
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppShellModule {
}
