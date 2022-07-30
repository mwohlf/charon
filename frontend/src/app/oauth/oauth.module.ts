import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {StoreModule} from '@ngrx/store';
import {featureKey, reducer} from './reducer';
import {AppThemeModule} from '../app-theme.module';
import {FlexLayoutModule} from '@angular/flex-layout';

@NgModule({
  imports: [
    AppThemeModule,
    CommonModule,
    StoreModule.forFeature(featureKey, reducer),
    // EffectsModule.forFeature([]),
    RouterModule,
    FlexLayoutModule,
  ],
  declarations: [
  ],
  exports: [
  ],
})

export class OauthModule {
}
