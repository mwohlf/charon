import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {EffectsModule} from '@ngrx/effects';
import {StoreModule} from '@ngrx/store';
import {Effects} from './effects';
import {featureKey, reducer} from './reducer';


@NgModule({
  imports: [
    CommonModule,
    StoreModule.forFeature(featureKey, reducer),
    EffectsModule.forFeature([
      Effects,
    ]),
  ],
})

export class ConfigModule {
}
