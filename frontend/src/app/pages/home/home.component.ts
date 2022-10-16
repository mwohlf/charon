import {Component, OnInit} from '@angular/core';
import {ConfigurationDetails} from 'build/generated/model/models';
import {Observable} from 'rxjs';
import {Store} from '@ngrx/store';
import {AppState} from '../../app-shell.module';
import {
  selectConfigurationDetails,
} from '../../modules/config/selector';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {

  static ROUTER_PATH: string = 'home';

   buildProperties$: Observable<ConfigurationDetails | undefined>;

  constructor(
    private store: Store<AppState>,
  ) {
    this.buildProperties$ = this.store.select(selectConfigurationDetails);
  }

  ngOnInit(): void {
  }

}
