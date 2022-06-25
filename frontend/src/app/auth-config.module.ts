import {HttpClient} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {AuthModule, LogLevel, StsConfigHttpLoader, StsConfigLoader} from 'angular-auth-oidc-client';
import {catchError, map} from 'rxjs/operators';
import {of} from "rxjs";

const OAUTH_CLIENT_CONFIG_URL = "http://localhost:4200/api/client-config-todo"; // TODO: implement a service in the backend module

const DEFAULT_CONFIG = {
  authority: "http://localhost:8081/authority",
  redirectUrl: window.location.origin,
  clientId: "messaging-client",
  responseType: 'code',
  scope: "message.read",
  postLogoutRedirectUri: "customConfig.post_logout_redirect_uri",
  startCheckSession: true,
  silentRenew: true,
  silentRenewUrl: '/silent-renew.html',
  postLoginRoute: "/home",
  forbiddenRoute: "/forbiddenRoute",
  unauthorizedRoute: "/unauthorizedRoute",
  logLevel: LogLevel.Debug,
  // maxIdTokenIatOffsetAllowedInSeconds: customConfig.max_id_token_iat_offset_allowed_in_seconds,
  historyCleanupOff: true,
  // autoUserInfo: false,
  // see: https://github.com/damienbod/angular-auth-oidc-client/blob/main/projects/sample-code-flow-auto-login/src/app/auth-config.module.ts
}


const config_mapper = (customConfig: any) => {
  return DEFAULT_CONFIG; // TODO
};


const error_config_mapper = (err: any, caught: any) => {
  console.warn(`I caught: ${err}, ${caught}, using default config for now`)
  return of(DEFAULT_CONFIG)
};

// TODO: we need an endpoint and a datatype for this
export const httpLoaderFactory = (httpClient: HttpClient) => {
  const config$ = httpClient.get<any>(OAUTH_CLIENT_CONFIG_URL).pipe(
    map(config_mapper),
    catchError(error_config_mapper)
  );

  return new StsConfigHttpLoader(config$);
};

@NgModule({
  imports: [
    AuthModule.forRoot({
      loader: {
        provide: StsConfigLoader,
        useFactory: httpLoaderFactory,
        deps: [HttpClient],
      },
    }),
  ],
  exports: [AuthModule],
})
export class AuthConfigModule {
}
