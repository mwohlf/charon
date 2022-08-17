import {CommonModule} from '@angular/common';
import {featureKey, reducer} from './reducer';
import {NgModule} from '@angular/core';
import {Store, StoreModule} from '@ngrx/store';
import {
  AuthInterceptor,
  AuthModule,
  StsConfigLoader,
} from 'angular-auth-oidc-client';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {
  OpenIdConfiguration,
} from 'angular-auth-oidc-client/lib/config/openid-configuration';
import {Observable} from 'rxjs';
import {AppState} from '../app-shell.module';
import {selectOpenIdConfigurations} from './selector';
import {filter, map} from 'rxjs/operators';
import * as _ from 'lodash';



class ConfigLoader {
  constructor(
    private passedConfigs: Observable<OpenIdConfiguration[]>,
  ) {
  };

  loadConfigs(): Observable<OpenIdConfiguration[]> {
    return this.passedConfigs;
  };
}

export const httpLoaderFactory = (store: Store<AppState>) => {
  // const config$ = of([CONFIG1, CONFIG2]);
  const config$ = store.select(selectOpenIdConfigurations)
    .pipe(
      filter(elements => elements.length > 0),
      map(elements => {
        return _.cloneDeep(elements);
      }),
    );

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
        deps: [Store<AppState>],
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
