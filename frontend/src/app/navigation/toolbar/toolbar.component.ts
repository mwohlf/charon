import {Component, OnInit} from '@angular/core';
import {OidcSecurityService, PublicEventsService} from "angular-auth-oidc-client";

@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.scss']
})
export class ToolbarComponent implements OnInit {

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
