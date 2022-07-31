import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {LoginResponse, OidcSecurityService} from 'angular-auth-oidc-client';


@Injectable({providedIn: 'root'})
export class RequestInterceptor implements HttpInterceptor {

  private accessToken: string | undefined;

  constructor(
    private oidcSecurityService: OidcSecurityService,
  ) {
    this.oidcSecurityService.checkAuth().subscribe(((next: LoginResponse) => {
      this.accessToken = next.accessToken;
      console.log('next: ', JSON.stringify(next));
    }));
  }

  // add bearer token if we have one
  public intercept(originalRequest: HttpRequest<any>, nextHandler: HttpHandler): Observable<HttpEvent<any>> {
    console.log('intercept: ', JSON.stringify(originalRequest));
    let newRequest = originalRequest;
    if (!!this.accessToken) {
      console.log('<intercept> adding bearer token for API access: ', originalRequest);
      newRequest = newRequest.clone({
        // adding bearer token for API access
        headers: newRequest.headers.set('Authorization', 'Bearer ' + this.accessToken),
      });
    }
    return nextHandler.handle(newRequest);
  }

}
