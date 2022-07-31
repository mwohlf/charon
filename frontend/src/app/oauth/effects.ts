import {Injectable} from '@angular/core';
import {NGXLogger} from 'ngx-logger';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {OidcSecurityService} from 'angular-auth-oidc-client';
import {Observable, of} from 'rxjs';
import {authorizeAction, logoffAction} from './action';
import {Action} from '@ngrx/store';
import {map, tap} from 'rxjs/operators';

@Injectable()
export class Effects {

  constructor(
    private oidcSecurityService: OidcSecurityService,
    private logger: NGXLogger,
    private action$: Actions,
  ) {
  }

  authorizeAction$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(authorizeAction),
      tap(() => {
        console.log('authorizeAction');
        // see: https://nice-hill-002425310.azurestaticapps.net/docs/documentation/public-api
        this.oidcSecurityService.authorize(); // this redirects
      }),
    );
  }, {dispatch: false});


  logoffAction$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(logoffAction),
      tap(() => {
        // see: https://nice-hill-002425310.azurestaticapps.net/docs/documentation/login-logout
        console.log('logoffAction...');
        // this.oidcSecurityService.logoff();
        // this.oidcSecurityService.logoffLocal();
        this.oidcSecurityService.logoffAndRevokeTokens()
          .subscribe((result) => console.log(result));
      }),
    );
  }, {dispatch: false});

}
