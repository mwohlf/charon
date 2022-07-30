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
        this.oidcSecurityService.authorize(); // this redirects
      }),
    );
  }, {dispatch: false});


  logoffAction$: Observable<Action> = createEffect(() => {
    return this.action$.pipe(
      ofType(logoffAction),
      tap(() => {
        console.log('logoffAction');
        this.oidcSecurityService.logoff();
      }),
    );
  }, {dispatch: false});

}
