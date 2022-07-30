import {CommonModule} from '@angular/common';
import {EffectsModule} from '@ngrx/effects';
import {Effects} from './effects';
import {featureKey, reducer} from './reducer';
import {NgModule} from '@angular/core';
import {StoreModule} from '@ngrx/store';


@NgModule({
  imports: [
    CommonModule,
    EffectsModule.forFeature([Effects]),
    StoreModule.forFeature(featureKey, reducer),
  ],
})

export class ConfigModule {
}
