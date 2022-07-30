import {AppThemeModule} from '../app-theme.module';
import {CommonModule} from '@angular/common';
import {EffectsModule} from '@ngrx/effects';
import {Effects} from './effects';
import {featureKey, reducer} from './reducer';
import {FlexLayoutModule} from '@angular/flex-layout';
import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {StoreModule} from '@ngrx/store';
import {AuthModule} from 'angular-auth-oidc-client';

@NgModule({
  imports: [
    AuthModule.forRoot({
      config: {
        configId: "simple",
        authority: 'http://127.0.0.1:9000',
        // localhost is not allowed here
        redirectUrl: 'http://127.0.0.1:4200/home',
        postLogoutRedirectUri: window.location.origin,
        clientId: 'public-client',
        scope: 'openid message.read message.write', // 'openid profile ' + your scopes
        responseType: 'code',
        silentRenew: false,
     //   silentRenewUrl: window.location.origin + '/silent-renew.html',
        renewTimeBeforeTokenExpiresInSeconds: 10,
        autoUserInfo: false,
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
})

export class OAuthModule {
}
