import {Component, OnInit} from '@angular/core';
import {ErrorComponent} from '../../pages/error/error.component';
import {HomeComponent} from '../../pages/home/home.component';
import {MainComponent} from '../../pages/main/main.component';
import {ProtectedComponent} from '../../pages/protected/protected.component';
import {Store} from '@ngrx/store';
import {AppState} from '../../app-shell.module';
import {setNavState} from '../../modules/view/action';
import {Observable} from 'rxjs';
import {MatDrawerMode} from '@angular/material/sidenav';
import {selectNavDrawMode} from '../../modules/view/selector';

@Component({
  selector: '.app-menu',
  templateUrl: './menu.component.html',
})
export class MenuComponent implements OnInit {

  prefix = '/';
  matDrawerMode$: Observable<MatDrawerMode>;

  public menuItems = [
    {
      'route': this.prefix + ErrorComponent.ROUTER_PATH,
      'icon': 'home',
      'title': 'Error',
    },
    {
      'route': this.prefix + HomeComponent.ROUTER_PATH,
      'icon': 'home',
      'title': 'Home',
    },
    {
      'route': this.prefix + MainComponent.ROUTER_PATH,
      'icon': 'home',
      'title': 'Main',
    },
    {
      'route': this.prefix + ProtectedComponent.ROUTER_PATH,
      'icon': 'home',
      'title': 'Protected',
    },
  ];

  // we need a custom implementation of the open/close trigger...
  //  - esc
  //  - backdrop click
  // ...to set the store state because we derive the open/close menu state from the store,
  // using default implementation gets us out of sync
  constructor(
    public store: Store<AppState>,
  ) {
    this.matDrawerMode$ = this.store.select(selectNavDrawMode);
  }


  ngOnInit(): void {
  }

  closeNavigation(matDrawerMode: MatDrawerMode) {
    if (matDrawerMode !== 'side') {
      this.store.dispatch(setNavState({payload: {navState: 'closed'}}));
    }
  }

}
