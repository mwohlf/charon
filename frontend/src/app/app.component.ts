import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from './app-shell.module';
import {isAuthenticated} from './oauth/selector';
import {selectNavPosition, selectNavState} from './view/selector';
import {Observable} from 'rxjs';
import {MatDrawerMode} from '@angular/material/sidenav';
import {NavState} from './view/reducer';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {

  isAuthenticated$: Observable<boolean | undefined>;
  navPosition$: Observable<MatDrawerMode>;
  navState$: Observable<NavState>;

  constructor(
    public store: Store<AppState>,
  ) {
    this.isAuthenticated$ = this.store.select(isAuthenticated);
    this.navPosition$ = this.store.select(selectNavPosition);
    this.navState$ = this.store.select(selectNavState);
  }

  ngOnInit() {
  }

}
