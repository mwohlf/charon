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
import {MatButtonModule} from '@angular/material/button';
import {AppThemeModule} from '../../app-theme.module';
import {AsyncPipe, NgIf} from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-header',
  imports: [
    MatIconModule,
    ThemePicker,
    MatButtonModule,
    AppThemeModule,
    AsyncPipe,
    NgIf,
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
