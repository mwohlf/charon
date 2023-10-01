import {Component, HostBinding, OnInit} from '@angular/core';
import {PageConfig} from '../page-config';
import {PageComponent} from '../../components/page/page.component';
import {MatCardModule} from '@angular/material/card';
import {AsyncPipe, NgIf} from '@angular/common';

@Component({
  imports: [
    PageComponent,
    MatCardModule,
    NgIf,
    AsyncPipe,
  ],
  standalone: true,
  selector: 'app-settings',
  templateUrl: './settings.component.html',
})
export class SettingsComponent implements OnInit {

  @HostBinding('class') class = 'main-content';

  static SPEC: PageConfig = {
    route: 'settings',
    icon: 'settings',
    title: 'Settings',
    requiredRoles: [],
    component: SettingsComponent,
    inMenu: () => {return true;},
  };

  ngOnInit(): void {
  }

}
