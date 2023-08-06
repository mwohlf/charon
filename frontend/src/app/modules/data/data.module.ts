import {CommonModule} from '@angular/common';
import {reducer} from './reducer';
import {NgModule} from '@angular/core';
import {StoreModule} from '@ngrx/store';
import {dataFeature} from '../../shared/const';


@NgModule({
  // Effects imported in main
  imports: [
    CommonModule,
    StoreModule.forFeature(dataFeature, reducer),
  ],
})

export class DataModule {
}
