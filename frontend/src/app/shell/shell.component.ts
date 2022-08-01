import {Component, OnInit} from '@angular/core';
import {
  EventTypes,
  LoginResponse, OidcClientNotification,
  OidcSecurityService,
  PublicEventsService,
} from 'angular-auth-oidc-client';
import {filter, tap} from 'rxjs/operators';

@Component({
  selector: 'app-shell',
  templateUrl: './shell.component.html',
})
export class ShellComponent implements OnInit {

  constructor(
    public oidcSecurityService: OidcSecurityService,
  ) {
  }

  ngOnInit() {
  }

}
