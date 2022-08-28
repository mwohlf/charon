import {
  ChangeDetectionStrategy,
  Component,
  OnDestroy,
  OnInit,
  ViewEncapsulation,
} from '@angular/core';
import {MatIconRegistry} from '@angular/material/icon';
import {Observable} from 'rxjs';
import {DomSanitizer} from '@angular/platform-browser';
import {LiveAnnouncer} from '@angular/cdk/a11y';
import {StyleManager} from './style-manager';
import {isDarkTheme, selectCurrentTheme} from './selector';
import {Store} from '@ngrx/store';
import {AppState} from '../app-shell.module';
import {configureTheme, toggleDarkMode} from './action';
import {ThemeDetails} from './reducer';


@Component({
  selector: 'theme-picker',
  templateUrl: 'theme-picker.html',
  // styleUrls: ['theme-picker.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
})
export class ThemePicker implements OnInit, OnDestroy {

    themes = [
    {
      primary: '#673AB7',
      accent: '#FFC107',
      displayName: 'Deep Purple & Amber',
      name: 'deeppurple-amber',
      isDark: false,
    },
    {
      primary: '#3F51B5',
      accent: '#E91E63',
      displayName: 'Indigo & Pink',
      name: 'indigo-pink',
      isDark: false,
    },
    {
      primary: '#E91E63',
      accent: '#607D8B',
      displayName: 'Pink & Blue-grey',
      name: 'pink-bluegrey',
      isDark: true,
    },
    {
      primary: '#9C27B0',
      accent: '#4CAF50',
      displayName: 'Purple & Green',
      name: 'purple-green',
      isDark: true,
    },
  ];


  isDarkTheme$: Observable<boolean | undefined>;

  currentTheme$: Observable<ThemeDetails>;

  constructor(
    public store: Store<AppState>,
    public styleManager: StyleManager,
    // private _themeStorage: ThemeStorage,
    private liveAnnouncer: LiveAnnouncer,
    iconRegistry: MatIconRegistry,
    sanitizer: DomSanitizer,
  ) {

    this.isDarkTheme$ = this.store.select(isDarkTheme);

    this.currentTheme$ = this.store.select(selectCurrentTheme);

    /*

iconRegistry.addSvgIcon('theme-example',
  sanitizer.bypassSecurityTrustResourceUrl(
    'assets/img/theme-demo-icon.svg'));
const themeName = this._themeStorage.getStoredThemeName();
if (themeName) {
  this.selectTheme(themeName);
} else {
  this.themes.find(themes => {
    if (themes.isDefault === true) {
      this.selectTheme(themes.name);
    }
  });
}
*/
  }

  ngOnInit() {
    this.currentTheme$.subscribe(theme => {
      this.styleManager.setStyle('theme', `${theme.name}.css`);
    })
  }

  ngOnDestroy() {
  }

  toggleDarkMode() {
    this.store.dispatch(toggleDarkMode());
  }

  selectTheme(themeName: string) {
    const nextTheme = this.themes.find(currentTheme => currentTheme.name === themeName);

    if (!nextTheme) {
      return;
    }
    this.store.dispatch(configureTheme({payload: nextTheme}));

    //this.currentTheme = theme;
    /*
    if (theme.isDefault) {
      this.styleManager.removeStyle('theme');
    } else {
      this.styleManager.setStyle('theme', `${theme.name}.css`);
    }
    if (this.currentTheme) {
      this.liveAnnouncer.announce(`${theme.displayName} theme selected.`, 'polite', 3000);
      // this._themeStorage.storeTheme(this.currentTheme);
    }
  */
  }
}

