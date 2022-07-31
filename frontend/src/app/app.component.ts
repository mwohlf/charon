import {Component, OnInit} from '@angular/core';
import {
  EventTypes, LoginResponse,
  OidcSecurityService,
  PublicEventsService,
} from 'angular-auth-oidc-client';
import {filter, tap} from 'rxjs/operators';
import {RequestInterceptor} from './oauth/request-interceptor';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {

  constructor(
    public oidcSecurityService: OidcSecurityService,
    public eventService: PublicEventsService,
  ) {
  }

  ngOnInit() {
    /*
    console.log('ngOnInit nexttt: ');
    // this reads the callback url params from oauth
    // apparently we are only supposed to call this once on initial load, or on auth redirect...
    this.oidcSecurityService.checkAuth().subscribe(((next: LoginResponse) => {
      console.error('nexttt: ', next);
      console.error('nexttt: ', next.accessToken);
      RequestInterceptor.ACCESS_TOKEN = next.accessToken;
      console.error('nexttt: ', RequestInterceptor.ACCESS_TOKEN);
    }));

     */
    /*
    this.oidcSecurityService.checkAuth().subscribe((next: LoginResponse) => {
      console.log('isAuthenticated: ', next.isAuthenticated);
      console.log('userData: ', next.userData);
      console.log('configId: ', next.configId);
      console.log('errorMessage: ', next.errorMessage);
      console.log(`Current accessToken: '${next.accessToken}'`);
      console.log(`Current idToken: '${next.idToken}'`);
    });

import { HttpHeaders } from '@angular/common/http';

const token = this.oidcSecurityServices.getAccessToken().subscribe((token) => {
  const httpOptions = {
    headers: new HttpHeaders({
      Authorization: 'Bearer ' + token,
    }),
  };
});
     */

    this.eventService
      .registerForEvents()
      .pipe(
        tap((elem) => {
          console.log("event: ", elem)
        } ),
        filter((notification) => notification.type === EventTypes.CheckSessionReceived)
      )
      .subscribe((value) => console.log('CheckSessionReceived with value from app ', value));
  }
}
