import {CommonModule} from '@angular/common';
import {reducer, SIMPLE_CONFIG} from './reducer';
import {NgModule} from '@angular/core';
import {Store, StoreModule} from '@ngrx/store';
import {
  AuthInterceptor,
  AuthModule, LogLevel,
  StsConfigHttpLoader,
  StsConfigLoader,
} from 'angular-auth-oidc-client';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {Observable, of, switchAll, switchMap} from 'rxjs';
import {
  OpenIdConfiguration
} from 'angular-auth-oidc-client/lib/config/openid-configuration';
import {AppState} from '../app-shell.module';
import {isAuthenticated, selectOpenIdConfigurations} from './selector';
import {map, mergeMap} from 'rxjs/operators';

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
}
const CONFIG2: OpenIdConfiguration = {
    configId: SIMPLE_CONFIG+"test",
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
}



export const stsConfigLoaderFactory = (store: Store<AppState>) => {
  const openIdConfigurations$ = store.select(selectOpenIdConfigurations)
    .pipe((elem) => { return of(elem)});

  const config01$ = of(CONFIG1);
  const config02$ = of(CONFIG2);
  return new StsConfigHttpLoader( // we need: Observable<OpenIdConfiguration>[]
    store.select(selectOpenIdConfigurations)  // observable of an array of config values
      .pipe(
        mergeMap((configurations: OpenIdConfiguration[]) => {
          // const resultarray: Observable<OpenIdConfiguration>[] =  configurations.map((config) => { return of(config)});
          // return resultarray;

          // const resultarray = [  {}, {} ]
          return [  {}, {} ];
        })
      )
  );
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
        useFactory: stsConfigLoaderFactory,
        deps: [Store<AppState>]
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


/*


 */
