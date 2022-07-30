import {Component, OnInit} from '@angular/core';
import {
  EventTypes, LoginResponse,
  OidcSecurityService,
  PublicEventsService,
} from 'angular-auth-oidc-client';
import {filter} from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {

  title = 'charon';

  constructor(
    private oidcSecurityService: OidcSecurityService,
    private eventService: PublicEventsService,
  ) {
    //
  }

  ngOnInit() {
    this.oidcSecurityService.checkAuth().subscribe((next: LoginResponse) => {
      console.log('isAuthenticated: ', next.isAuthenticated);
      console.log('userData: ', next.userData);
      console.log('configId: ', next.configId);
      console.log('errorMessage: ', next.errorMessage);
      console.log(`Current accessToken: '${next.accessToken}'`);
      console.log(`Current idToken: '${next.idToken}'`);
    });

    this.eventService
      .registerForEvents()
      .pipe(filter((notification) => notification.type === EventTypes.CheckSessionReceived))
      .subscribe((value) => console.log('CheckSessionReceived with value from app ', value));
  }
}
