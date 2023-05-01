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
import {MatIconModule} from '@angular/material/icon';
import {RouterLink, RouterLinkActive} from '@angular/router';
import {MatListModule} from '@angular/material/list';
import {AsyncPipe, NgForOf, NgIf} from '@angular/common';
import {PageConfig} from '../../pages/page-config';
import {SettingsComponent} from '../../pages/settings/settings.component';

@Component({
  imports: [
    MatIconModule,
    RouterLink,
    MatListModule,
    RouterLinkActive,
    NgForOf,
    NgIf,
    AsyncPipe,
  ],
  selector: 'app-menu',
  standalone: true,
  templateUrl: './menu.component.html',
})
export class MenuComponent implements OnInit {

  static PREFIX = `/`;
  matDrawerMode$: Observable<MatDrawerMode>;


  public menuItems: PageConfig[] = [
    HomeComponent.SPEC,
    SettingsComponent.SPEC,
    //  ErrorComponent.SPEC,
    //   MainComponent.SPEC,
    //  ProtectedComponent.SPEC,
  ].map((spec: PageConfig) => {
    return {
      ...spec,
      // fix the route
      'route': MenuComponent.PREFIX + spec.route,
    };
  });

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

  isVisible(item: PageConfig): boolean {
    return true;
  }

  closeNavigation(matDrawerMode: MatDrawerMode) {
    if (matDrawerMode !== 'side') {
      this.store.dispatch(setNavState({payload: {navState: 'closed'}}));
    }
  }

}
