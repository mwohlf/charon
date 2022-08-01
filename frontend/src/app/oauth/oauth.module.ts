import {AppThemeModule} from '../app-theme.module';
import {CommonModule} from '@angular/common';
import {EffectsModule} from '@ngrx/effects';
import {Effects} from './effects';
import {reducer, SIMPLE_CONFIG} from './reducer';
import {FlexLayoutModule} from '@angular/flex-layout';
import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {StoreModule} from '@ngrx/store';
import {AuthInterceptor, AuthModule, LogLevel} from 'angular-auth-oidc-client';
import {HTTP_INTERCEPTORS} from '@angular/common/http';

export const featureKey = 'oAuthFeature';


@NgModule({
  imports: [
    // see: https://nice-hill-002425310.azurestaticapps.net/docs/documentation/configuration
    // config from here: https://github.com/damienbod/angular-auth-oidc-client/issues/1318
    AuthModule.forRoot({
      config: {
        configId: SIMPLE_CONFIG,
        authority: 'http://127.0.0.1:8081',
        redirectUrl: 'http://127.0.0.1:4200/home',
        postLogoutRedirectUri: window.location.origin,
        clientId: 'public-client',
        scope: 'openid profile email offline_access',
        responseType: 'code',
        silentRenew: true,
        useRefreshToken: true,
        logLevel: LogLevel.Debug,
        autoUserInfo: false,

        secureRoutes: [
          '/api',
          '/oauth2',
          'https://localhost/',
          'https://127.0.0.1/',
          'https://localhost:8080/',
          'https://127.0.0.1:8080/',
          'https://localhost:4200/',
          'https://127.0.0.1:4200/',
          'http://127.0.0.1:8081/oauth2/revoke',
        ],

      },
    }),
    AppThemeModule,
    CommonModule,
    CommonModule,
    EffectsModule.forFeature([Effects]),
    FlexLayoutModule,
    RouterModule,
    StoreModule.forFeature(featureKey, reducer),
  ],
  declarations: [],
  exports: [
    AuthModule,
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
  ],
})

export class OAuthModule {
}
