import {HttpClient} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {AuthModule, LogLevel, StsConfigHttpLoader, StsConfigLoader} from 'angular-auth-oidc-client';
import {catchError, map} from 'rxjs/operators';
import {of} from "rxjs";

@NgModule({
  imports: [AuthModule.forRoot({
    config: {
      authority: 'http://localhost:9000/issuer',
      redirectUrl: window.location.origin,
      postLogoutRedirectUri: window.location.origin,
      clientId: 'public-client',
      scope: 'openid message.read message.write', // 'openid profile ' + your scopes
      responseType: 'code',
      silentRenew: true,
      silentRenewUrl: window.location.origin + '/silent-renew.html',
      renewTimeBeforeTokenExpiresInSeconds: 10,
      autoUserInfo: false
    }
  })],
  exports: [AuthModule],
})
export class AuthConfigModule {
}
