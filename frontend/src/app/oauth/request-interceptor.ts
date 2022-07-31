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

  // TODO: seems like this is not a singleton and this constructor is called multiple times...
  constructor(
  ) {
    console.error('new RequestInterceptor');
  }

  static setAccessToken(accessToken: string) {
    console.error('setAccessToken', accessToken);
    RequestInterceptor.ACCESS_TOKEN = accessToken;
  }

  // add bearer token if we have one
  public intercept(originalRequest: HttpRequest<any>, nextHandler: HttpHandler): Observable<HttpEvent<any>> {
    console.error('intercept: ', JSON.stringify(originalRequest.url));
    console.error('token: ', RequestInterceptor.ACCESS_TOKEN);

    let newRequest = originalRequest;
    if (!!RequestInterceptor.ACCESS_TOKEN) {
      console.error('<intercept> adding bearer token for API access: ', originalRequest.url);
      newRequest = newRequest.clone({
        // adding bearer token for API access
        headers: newRequest.headers.set('Authorization', 'Bearer ' + RequestInterceptor.ACCESS_TOKEN),
      });
    }
    return nextHandler.handle(newRequest);
  }


}
