import {CommonModule} from '@angular/common';
import {featureKey, reducer} from './reducer';
import {NgModule} from '@angular/core';
import {StoreModule} from '@ngrx/store';


@NgModule({
  // Effects imported in main
  imports: [
    CommonModule,
    StoreModule.forFeature(featureKey, reducer),
  ],
})

export class ConfigModule {
}
