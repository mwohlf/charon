import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import {Injectable, Injector} from '@angular/core';
import {Observable} from 'rxjs';
import {LoginResponse, OidcSecurityService} from 'angular-auth-oidc-client';


@Injectable({providedIn: 'root'})
export class RequestInterceptor implements HttpInterceptor {

  public static ACCESS_TOKEN: string | undefined;

  constructor(
  ) {
    console.error('********************* constructor');
  }

  // add bearer token if we have one
  public intercept(originalRequest: HttpRequest<any>, nextHandler: HttpHandler): Observable<HttpEvent<any>> {
    console.log('intercept: ', JSON.stringify(originalRequest));
    console.error('********************* intercept ', RequestInterceptor.ACCESS_TOKEN);

    let newRequest = originalRequest;
    if (!!RequestInterceptor.ACCESS_TOKEN) {
      console.error('<intercept> adding bearer token for API access: ', originalRequest);
      newRequest = newRequest.clone({
        // adding bearer token for API access
        headers: newRequest.headers.set('Authorization', 'Bearer ' + RequestInterceptor.ACCESS_TOKEN),
      });
    }
    return nextHandler.handle(newRequest);
  }

}
