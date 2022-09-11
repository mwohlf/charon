import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from './app-shell.module';
import {isAuthenticated} from './oauth/selector';
import {selectNavDrawMode, selectNavState} from './view/selector';
import {Observable, ReplaySubject, takeUntil} from 'rxjs';
import {MatDrawerMode} from '@angular/material/sidenav';
import {NavState} from './view/reducer';
import {BreakpointObserver} from '@angular/cdk/layout';
import {menuWidth, mobileBreakpoint} from './const';
import {map} from 'rxjs/operators';
import {setNavDrawMode, setNavState} from './view/action';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  private destroyed$: ReplaySubject<boolean> = new ReplaySubject(1);

  isAuthenticated$: Observable<boolean | undefined>;
  navPosition$: Observable<MatDrawerMode>;
  navState$: Observable<NavState>;
  menuWidth: string = menuWidth + 'px';
  mobileBreakpoint: string = mobileBreakpoint + 'px';

  constructor(
    private breakpointObserver: BreakpointObserver,
    public store: Store<AppState>,
  ) {
    this.isAuthenticated$ = this.store.select(isAuthenticated);
    this.navPosition$ = this.store.select(selectNavDrawMode);
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
        console.log('largerThanMin: ', largerThanMin);
        if (largerThanMin) {
          this.store.dispatch(setNavState({payload: {navState: 'opened'}}));
          this.store.dispatch(setNavDrawMode({payload: {navDrawMode: 'side'}}));
        } else {
          this.store.dispatch(setNavState({payload: {navState: 'closed'}}));
          this.store.dispatch(setNavDrawMode({payload: {navDrawMode: 'over'}}));
        }
      });
  }

  ngOnDestroy() {
    this.destroyed$.next(true);
    this.destroyed$.complete();
  }
}
