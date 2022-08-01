import {Component, OnInit} from '@angular/core';
import {
  OidcSecurityService,
  PublicEventsService,
} from 'angular-auth-oidc-client';

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
  }

}
