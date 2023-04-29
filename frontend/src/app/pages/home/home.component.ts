import {Component, OnInit} from '@angular/core';
import {ConfigurationDetails} from 'build/generated/model/models';
import {Observable} from 'rxjs';
import {Store} from '@ngrx/store';
import {AppState} from '../../app-shell.module';
import {
  selectConfigurationDetails,
} from '../../modules/config/selector';
import {page_spec} from '../page.spec';
import {PageComponent} from '../../components/page/page.component';
import {MatCardModule} from '@angular/material/card';
import {AsyncPipe, NgIf} from '@angular/common';
import {
  isAuthenticated,
  selectUserData,
  selectUserName,
} from '../../modules/oauth/selector';

@Component({
  imports: [
    PageComponent,
    MatCardModule,
    NgIf,
    AsyncPipe,
  ],
  standalone: true,
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {

  static SPEC: page_spec = {
    route: 'home',
    icon: 'home',
    title: 'Home',
    component: HomeComponent,
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
  }

  ngOnInit(): void {
  }

}
