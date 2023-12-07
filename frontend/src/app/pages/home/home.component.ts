import {Component, HostBinding, OnInit} from '@angular/core';
import {ConfigurationDetails} from 'build/generated/model/models';
import {Observable} from 'rxjs';
import {Store} from '@ngrx/store';
import {AppState} from '../../app-shell.module';
import {selectConfigurationDetails} from '../../modules/config/selector';
import {PageConfig} from '../page-config';
import {PageComponent} from '../../components/page/page.component';
import {MatCardModule} from '@angular/material/card';
import {AsyncPipe, DatePipe, NgIf} from '@angular/common';
import {isAuthenticated, selectUserName} from '../../modules/oauth/selector';
import {multiply} from 'lodash-es';

@Component({
  imports: [
    PageComponent,
    MatCardModule,
    NgIf,
    AsyncPipe,
    DatePipe,
  ],
  standalone: true,
  selector: 'app-home',
  templateUrl: './home.component.html',
})
export class HomeComponent implements OnInit {

  @HostBinding('class') class = 'main-content';

  static SPEC: PageConfig = {
    route: 'home',
    icon: 'home',
    title: 'Home',
    requiredRoles: [],
    component: HomeComponent,
    inMenu: () => {return true;},
  };

  buildProperties$: Observable<ConfigurationDetails | undefined>;
  userName$: Observable<string | undefined>;
  isAuthenticated$: Observable<boolean>;

  constructor(
    private store: Store<AppState>,
  ) {
    this.buildProperties$ = this.store.select(selectConfigurationDetails);
    this.isAuthenticated$ = this.store.select(isAuthenticated);
    this.userName$ = this.store.select(selectUserName);

    // new Date(unixTimestamp * 1000)
  }

  ngOnInit(): void {
  }

  protected readonly multiply = multiply;

  protected readonly parseFloat = parseFloat;
}
