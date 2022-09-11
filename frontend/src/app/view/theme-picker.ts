import {
  ChangeDetectionStrategy,
  Component,
  ViewEncapsulation,
} from '@angular/core';
import {Observable} from 'rxjs';
import {selectCurrentTheme} from './selector';
import {Store} from '@ngrx/store';
import {AppState} from '../app-shell.module';
import {setThemeDetails} from './action';
import {ThemeDetails} from './reducer';
import {themes} from './themelist';


@Component({
  selector: 'theme-picker',
  templateUrl: 'theme-picker.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
})
export class ThemePicker {

  currentTheme$: Observable<ThemeDetails>;
  themes = themes;

  constructor(
    public store: Store<AppState>,
  ) {
    this.currentTheme$ = this.store.select(selectCurrentTheme);
  }

  toggleDarkMode(theTheme: ThemeDetails) {
    console.log('toggleDarkMode');
    let nextTheme = {
      ...theTheme,
    };
    nextTheme.variant = (nextTheme.variant == 'dark') ? 'light' : 'dark';
    this.store.dispatch(setThemeDetails({payload: nextTheme}));
  }

  selectTheme(theTheme: ThemeDetails, nextName: string) {
    console.log('selectTheme');
    let theme = themes.find(element => element.name === nextName);
    if (!theme) {
      theme = themes[0];
    }
    let nextTheme = {
      ...theTheme,
      name: theme.name,
      displayName: theme.displayName,
    };
    this.store.dispatch(setThemeDetails({payload: nextTheme}));
  }

}

