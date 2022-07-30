import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {StoreModule} from '@ngrx/store';
import {featureKey, reducer} from './reducer';
import {HeaderComponent} from './header.component';
import {AppThemeModule} from '../app-theme.module';

@NgModule({
  imports: [
    AppThemeModule,
    CommonModule,
    StoreModule.forFeature(featureKey, reducer),
    // EffectsModule.forFeature([]),
    RouterModule,
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
