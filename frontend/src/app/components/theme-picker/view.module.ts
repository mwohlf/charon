import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatMenuModule} from '@angular/material/menu';
import {MatTooltipModule} from '@angular/material/tooltip';
import {ThemePicker} from './theme-picker';
import {StyleManager} from '../../modules/view/style-manager';
import {StoreModule} from '@ngrx/store';
import {reducer} from '../../modules/view/reducer';
import {viewFeature} from '../../shared/const';
import {LayoutModule} from '@angular/cdk/layout';


@NgModule({
  imports: [
    CommonModule,
    LayoutModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatTooltipModule,
    StoreModule.forFeature(viewFeature, reducer),
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
export class ViewModule {
}
