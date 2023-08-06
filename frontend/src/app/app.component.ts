import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from './app-shell.module';
import {isAuthenticated} from './modules/oauth/selector';
import {selectNavDrawMode, selectNavState} from './modules/view/selector';
import {
  distinctUntilChanged,
  first,
  Observable,
  ReplaySubject, startWith,
  takeUntil,
} from 'rxjs';
import {MatDrawerMode} from '@angular/material/sidenav';
import {NavState} from './modules/view/reducer';
import {
  BreakpointObserver,
  Breakpoints,
  BreakpointState,
} from '@angular/cdk/layout';
import {menuWidth, mobileBreakpoint} from './shared/const';
import {map, tap} from 'rxjs/operators';
import {
  setBreakpoint,
  setNavDrawMode,
  setNavState,
} from './modules/view/action';
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
      .observe([Breakpoints.XSmall, Breakpoints.Small, Breakpoints.Medium, Breakpoints.Large, Breakpoints.XLarge])
      .pipe(
        takeUntil(this.destroyed$),
        distinctUntilChanged(),
        tap((value: BreakpointState) => this.logger.debug('<breakpointObserver> ngOnInit tap value: ', value)),
        // just the event, not the data here
      )
      .subscribe((): void => {
        if (this.breakpointObserver.isMatched(Breakpoints.XSmall)) {
          this.store.dispatch(setBreakpoint({payload: {breakpoint: 'small'}}));
        } else if (this.breakpointObserver.isMatched(Breakpoints.Small)) {
          this.store.dispatch(setBreakpoint({payload: {breakpoint: 'small'}}));
        } else if(this.breakpointObserver.isMatched(Breakpoints.Medium)) {
          this.store.dispatch(setBreakpoint({payload: {breakpoint: 'medium'}}));
        } else if(this.breakpointObserver.isMatched(Breakpoints.Large)) {
          this.store.dispatch(setBreakpoint({payload: {breakpoint: 'large'}}));
        } else if(this.breakpointObserver.isMatched(Breakpoints.XLarge)) {
          this.store.dispatch(setBreakpoint({payload: {breakpoint: 'large'}}));
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
