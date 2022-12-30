import {Component, OnInit} from '@angular/core';
import {ConfigurationDetails} from 'build/generated/model/models';
import {Observable} from 'rxjs';
import {Store} from '@ngrx/store';
import {AppState} from '../../app-shell.module';
import {
  selectConfigurationDetails,
} from '../../modules/config/selector';
import {page_spec} from '../page.spec';

@Component({
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
  }

   buildProperties$: Observable<ConfigurationDetails | undefined>;

  constructor(
    private store: Store<AppState>,
  ) {
    this.buildProperties$ = this.store.select(selectConfigurationDetails);
  }

  ngOnInit(): void {
  }

}
