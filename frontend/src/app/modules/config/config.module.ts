import {CommonModule} from '@angular/common';
import {reducer} from './reducer';
import {NgModule} from '@angular/core';
import {StoreModule} from '@ngrx/store';
import {configFeature} from '../../const';


@NgModule({
  // Effects imported in main
  imports: [
    CommonModule,
    StoreModule.forFeature(configFeature, reducer),
  ],
})

export class ConfigModule {
}
