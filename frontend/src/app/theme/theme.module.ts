import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatMenuModule} from '@angular/material/menu';
import {MatTooltipModule} from '@angular/material/tooltip';
import {ThemePicker} from './theme-picker';
import {StyleManager} from './style-manager';
import {StoreModule} from '@ngrx/store';
import {featureKey, reducer} from './reducer';

@NgModule({
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatTooltipModule,
    StoreModule.forFeature(featureKey, reducer),
  ],
  exports: [
    ThemePicker,
  ],
  declarations: [
    ThemePicker,
  ],
  providers: [
    StyleManager,
  ],
})
export class ThemeModule {
}
