import {CommonModule} from '@angular/common';
import {reducer} from './reducer';
import {NgModule} from '@angular/core';
import {StoreModule} from '@ngrx/store';
import {configFeature} from '../../shared/const';


@NgModule({
  // Effects imported in main EffectsModule.forRoot
  imports: [
    CommonModule,
    StoreModule.forFeature(configFeature, reducer),
  ],
})

// Module imported in main imports
export class ConfigModule {
}
