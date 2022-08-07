import {CommonModule} from '@angular/common';
import {reducer, SIMPLE_CONFIG} from './reducer';
import {NgModule} from '@angular/core';
import {StoreModule} from '@ngrx/store';
import {
  AuthInterceptor,
  AuthModule,
  LogLevel,
  StsConfigLoader,
} from 'angular-auth-oidc-client';
import {HTTP_INTERCEPTORS, HttpClient} from '@angular/common/http';
import {
  OpenIdConfiguration,
} from 'angular-auth-oidc-client/lib/config/openid-configuration';
import {Observable, of} from 'rxjs';

export const featureKey = 'oAuthFeature';


const CONFIG1: OpenIdConfiguration = {
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
};
const CONFIG2: OpenIdConfiguration = {
  configId: SIMPLE_CONFIG + 'test',
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
};


class ConfigLoader {
  constructor(
    private passedConfigs: Observable<OpenIdConfiguration[]>,
  ) {
  };

  loadConfigs(): Observable<OpenIdConfiguration[]> {
    return this.passedConfigs;
  };
}

export const httpLoaderFactory = (httpClient: HttpClient) => {
  const config$ = of([CONFIG1, CONFIG2]);
  return new ConfigLoader(config$);
};


@NgModule({
  // Effects imported in main
  imports: [
    CommonModule,
    // see: https://nice-hill-002425310.azurestaticapps.net/docs/documentation/configuration
    // config from here: https://github.com/damienbod/angular-auth-oidc-client/issues/1318
    AuthModule.forRoot({
      loader: {
        provide: StsConfigLoader,
        useFactory: httpLoaderFactory,
        deps: [HttpClient],
      },
    }),
    StoreModule.forFeature(featureKey, reducer),
  ],
  exports: [
    AuthModule,
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
  ],
})

export class OAuthModule {
}
