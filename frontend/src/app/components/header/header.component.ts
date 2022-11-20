import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from '../../app-shell.module';
import {loginAction, logoutAction} from '../../modules/oauth/action';
import {Observable} from 'rxjs';
import {isAuthenticated} from '../../modules/oauth/selector';
import {readConfigurationDetailsUsingGET} from '../../modules/config/action';
import {SIMPLE_CONFIG} from '../../modules/oauth/reducer';
import {setNavState, toggleMenu} from '../../modules/view/action';
import {selectNavDrawMode, selectNavState} from '../../modules/view/selector';
import {MatDrawerMode} from '@angular/material/sidenav';
import {NavState} from '../../modules/view/reducer';
import {MatIconModule} from '@angular/material/icon';
import {ThemePicker} from '../theme-picker/theme-picker';
import {CommonModule} from '@angular/common';
import {MatButtonModule} from '@angular/material/button';
import {AppThemeModule} from '../../app-theme.module';

@Component({
  standalone: true,
  selector: 'app-header',
  imports: [
    CommonModule,
    MatIconModule,
    ThemePicker,
    MatButtonModule,
    AppThemeModule,
  ],
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
    console.log("reReadConfig");
    this.store.dispatch(readConfigurationDetailsUsingGET());
  }

  toggleMenu() {
    console.log("toggleMenu");
    this.store.dispatch(toggleMenu());
  }

  setNavState(navState: NavState) {
    console.log("setNavState");
    this.store.dispatch(setNavState({payload: {navState: navState}}));
  }

  login() {
    console.log("login");
    this.store.dispatch(loginAction({payload: {configId: SIMPLE_CONFIG}}));
  }

  logout() {
    console.log("logout");
    this.store.dispatch(logoutAction());
  }

}
