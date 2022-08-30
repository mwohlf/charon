import {
  ChangeDetectionStrategy,
  Component,
  ViewEncapsulation,
} from '@angular/core';
import {Observable} from 'rxjs';
import {selectCurrentTheme} from './selector';
import {Store} from '@ngrx/store';
import {AppState} from '../app-shell.module';
import {configureTheme} from './action';
import {ThemeDetails} from './reducer';


@Component({
  selector: 'theme-picker',
  templateUrl: 'theme-picker.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
})
export class ThemePicker {

  themes = [
    {
      displayName: 'Deep Purple & Amber',
      name: 'deeppurple-amber',
    },
    {
      displayName: 'Indigo & Pink',
      name: 'indigo-pink',
    },
    {
      displayName: 'Pink & Blue-grey',
      name: 'pink-bluegrey',
    },
    {
      displayName: 'Purple & Green',
      name: 'purple-green',
    },
  ];

  currentTheme$: Observable<ThemeDetails>;

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
    nextTheme.isDark = !nextTheme.isDark;
    this.store.dispatch(configureTheme({payload: nextTheme}));
  }

  selectTheme(theTheme: ThemeDetails, nextName: string) {
    console.log('selectTheme');
    let theme = this.themes.find(element => element.name === nextName);
    if (!theme) {
      theme = this.themes[0];
    }
    let nextTheme = {
      ...theTheme,
      name: theme.name,
      displayName: theme.displayName,
    };
    this.store.dispatch(configureTheme({payload: nextTheme}));
  }

}

