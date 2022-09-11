import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from '../app-shell.module';
import {loginAction, logoutAction} from '../oauth/action';
import {Observable} from 'rxjs';
import {isAuthenticated} from '../oauth/selector';
import {readConfigurationDetailsUsingGET} from '../config/action';
import {SIMPLE_CONFIG} from '../oauth/reducer';
import {setNavState, toggleMenu} from '../view/action';
import {selectNavDrawMode, selectNavState} from '../view/selector';
import {MatDrawerMode} from '@angular/material/sidenav';
import {NavState} from '../view/reducer';

@Component({
  selector: '.app-header',
  templateUrl: './header.component.html',
})
export class HeaderComponent implements OnInit {

  isAuthenticated$: Observable<boolean | undefined>;
  navState$: Observable<NavState>;
  navDrawMode$: Observable<MatDrawerMode>;

  constructor(
    public store: Store<AppState>,
  ) {
    this.isAuthenticated$ = this.store.select(isAuthenticated);
    this.navState$ = this.store.select(selectNavState);
    this.navDrawMode$ = this.store.select(selectNavDrawMode);
  }

  ngOnInit(): void {
  }

  reReadConfig() {
    this.store.dispatch(readConfigurationDetailsUsingGET());
  }

  toggleMenu() {
    this.store.dispatch(toggleMenu());
  }

  setNavState(navState: NavState) {
    this.store.dispatch(setNavState({payload: {navState: navState}}));
  }

  login() {
    this.store.dispatch(loginAction({payload: {configId: SIMPLE_CONFIG}}));
  }

  logout() {
    this.store.dispatch(logoutAction());
  }

}
