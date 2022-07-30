import {AppThemeModule} from '../app-theme.module';
import {CommonModule} from '@angular/common';
import {EffectsModule} from '@ngrx/effects';
import {Effects} from './effects';
import {featureKey, reducer} from './reducer';
import {FlexLayoutModule} from '@angular/flex-layout';
import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {StoreModule} from '@ngrx/store';
import {AuthModule, LogLevel} from 'angular-auth-oidc-client';

@NgModule({
  imports: [
    // see: https://nice-hill-002425310.azurestaticapps.net/docs/documentation/configuration
    // config from here: https://github.com/damienbod/angular-auth-oidc-client/issues/1318
    AuthModule.forRoot({
      config: {
        configId: "simple",
        //   autoUserInfo: false,
        //   silentRenew: false,
        //   silentRenewUrl: window.location.origin + '/silent-renew.html',
        //   localhost is not allowed here
        authority: 'http://127.0.0.1:8081',
        clientId: 'public-client',
        logLevel: LogLevel.Debug,
        postLogoutRedirectUri: window.location.origin,
        redirectUrl: 'http://127.0.0.1:4200/home',
        renewTimeBeforeTokenExpiresInSeconds: 10,
        responseType: 'code',
        scope: 'openid',
        // scope: 'openid message.read message.write', // 'openid profile ' + your scopes
        useRefreshToken: true,
        issValidationOff: false,
        historyCleanupOff: false,
        startCheckSession: true,
        silentRenew: true,
      }
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
})

export class OAuthModule {
}
