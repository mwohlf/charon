import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from '../../app-shell.module';
import {
  loginAction,
  logoutAction,
  registerAction,
} from '../../modules/oauth/action';
import {Observable} from 'rxjs';
import {isAuthenticated, selectUserData} from '../../modules/oauth/selector';
import {readConfigurationDetailsUsingGET} from '../../modules/config/action';
import {SIMPLE_CONFIG} from '../../modules/oauth/reducer';
import {setNavState, toggleMenu} from '../../modules/view/action';
import {selectNavDrawMode, selectNavState} from '../../modules/view/selector';
import {MatDrawerMode} from '@angular/material/sidenav';
import {NavState} from '../../modules/view/reducer';
import {MatIconModule} from '@angular/material/icon';
import {ThemePicker} from '../theme-picker/theme-picker';
import {MatButtonModule} from '@angular/material/button';
import {AppThemeModule} from '../../app-theme.module';
import {AsyncPipe, JsonPipe, NgIf} from '@angular/common';
import {NGXLogger} from 'ngx-logger';

@Component({
  imports: [
    MatIconModule,
    ThemePicker,
    MatButtonModule,
    AppThemeModule,
    AsyncPipe,
    JsonPipe,
    NgIf,
  ],
  selector: 'app-header',
  standalone: true,
  templateUrl: './header.component.html',
})
export class HeaderComponent implements OnInit {

  isAuthenticated$: Observable<boolean | undefined>;
  navState$: Observable<NavState>;
  navDrawMode$: Observable<MatDrawerMode>;
  userData$: Observable<string>;

  constructor(
    public store: Store<AppState>,
    private logger: NGXLogger,
  ) {
    this.isAuthenticated$ = this.store.select(isAuthenticated);
    this.navState$ = this.store.select(selectNavState);
    this.navDrawMode$ = this.store.select(selectNavDrawMode);
    this.userData$ = this.store.select(selectUserData);
  }

  ngOnInit(): void {
  }

  reReadConfig() {
    console.info('reReadConfig');
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

  register() {
    this.store.dispatch(registerAction({payload: {configId: SIMPLE_CONFIG}}));
  }

  logout() {
    this.store.dispatch(logoutAction());
  }

}
