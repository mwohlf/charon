import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from './app-shell.module';
import {isAuthenticated} from './modules/oauth/selector';
import {selectNavDrawMode, selectNavState} from './modules/view/selector';
import {Observable, ReplaySubject, takeUntil} from 'rxjs';
import {MatDrawerMode} from '@angular/material/sidenav';
import {NavState} from './modules/view/reducer';
import {BreakpointObserver} from '@angular/cdk/layout';
import {menuWidth, mobileBreakpoint} from './shared/const';
import {map} from 'rxjs/operators';
import {setNavDrawMode, setNavState} from './modules/view/action';
import {NGXLogger} from 'ngx-logger';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  private destroyed$: ReplaySubject<boolean> = new ReplaySubject(1);

  isAuthenticated$: Observable<boolean | undefined>;
  matDrawerMode$: Observable<MatDrawerMode>;
  navState$: Observable<NavState>;
  menuWidth: string = menuWidth + 'px';
  mobileBreakpoint: string = mobileBreakpoint + 'px';

  constructor(
    public store: Store<AppState>,
    private logger: NGXLogger,
    private breakpointObserver: BreakpointObserver,
  ) {
    this.isAuthenticated$ = this.store.select(isAuthenticated);
    this.matDrawerMode$ = this.store.select(selectNavDrawMode);
    this.navState$ = this.store.select(selectNavState);
  }

  ngOnInit() {
    this.breakpointObserver
      .observe(`(min-width: ${this.mobileBreakpoint})`)
      .pipe(
        takeUntil(this.destroyed$),
        map(({matches}) => matches),
      )
      .subscribe((largerThanMin: boolean) => {
        this.logger.debug('<ngOnInit> largerThanMin: ', largerThanMin);
        if (largerThanMin) {
          // enough space, menu and content
          this.store.dispatch(setNavState({payload: {navState: 'opened'}}));
          // exist side by side
          this.store.dispatch(setNavDrawMode({payload: {navDrawMode: 'side'}}));
        } else {
          // mobile screen, menu initially closed
          this.store.dispatch(setNavState({payload: {navState: 'closed'}}));
          // opening menu hides and disables content, 'push' disables and pusheds content
          this.store.dispatch(setNavDrawMode({payload: {navDrawMode: 'over'}}));
        }
      });
  }

  ngOnDestroy() {
    this.destroyed$.next(true);
    this.destroyed$.complete();
  }

  closeNavigation(matDrawerMode: MatDrawerMode) {
    this.store.dispatch(setNavState({payload: {navState: 'closed'}}));
  }

}
