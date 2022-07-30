import {Component, OnInit} from '@angular/core';
import {OidcSecurityService, PublicEventsService} from "angular-auth-oidc-client";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
})
export class HeaderComponent implements OnInit {

  constructor(
    private oidcSecurityService: OidcSecurityService,
    private eventService: PublicEventsService,
  ) {
    //
  }

  ngOnInit(): void {
  }

  login() {
    this.oidcSecurityService.authorize();
  }

  logout() {
    this.oidcSecurityService.logoff();
  }
}
