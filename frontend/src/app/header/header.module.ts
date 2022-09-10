import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {StoreModule} from '@ngrx/store';
import {reducer} from './reducer';
import {HeaderComponent} from './header.component';
import {AppThemeModule} from '../app-theme.module';
import {FlexLayoutModule} from '@angular/flex-layout';
import {ViewModule} from '../view/view.module';
import {headerFeature} from '../const';


@NgModule({
  imports: [
    AppThemeModule,
    CommonModule,
    StoreModule.forFeature(headerFeature, reducer),
    RouterModule,
    FlexLayoutModule,
    ViewModule,
  ],
  declarations: [
    HeaderComponent,
  ],
  exports: [
    HeaderComponent,
  ],
})

export class HeaderModule {
}
