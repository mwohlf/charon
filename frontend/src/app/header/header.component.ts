import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from '../app-shell.module';
import {loginAction, logoutAction} from '../oauth/action';
import {Observable} from 'rxjs';
import {isAuthenticated} from '../oauth/selector';
import {readConfigurationDetailsUsingGET} from '../config/action';
import {SIMPLE_CONFIG} from '../oauth/reducer';
import {setNavPosition} from '../view/action';
import {selectNavPosition} from '../view/selector';

@Component({
  selector: '.app-header',
  templateUrl: './header.component.html',
})
export class HeaderComponent implements OnInit {

  isAuthenticated$: Observable<boolean | undefined>;
  navPosition$: Observable<'side' | 'over'>;

  constructor(
    public store: Store<AppState>,
  ) {
    this.isAuthenticated$ = this.store.select(isAuthenticated);
    this.navPosition$ = this.store.select(selectNavPosition);
  }

  ngOnInit(): void {
  }

  reReadConfig() {
    this.store.dispatch(readConfigurationDetailsUsingGET());
  }

  setNavPosition(position: string) {
    this.store.dispatch(setNavPosition({payload: {position: position}}));
  }

  login() {
    this.store.dispatch(loginAction({payload: {configId: SIMPLE_CONFIG}}));
  }

  logout() {
    this.store.dispatch(logoutAction());
  }

}
