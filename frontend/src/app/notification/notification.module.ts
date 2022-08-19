import {CommonModule} from '@angular/common';
import {featureKey, reducer} from './reducer';
import {NgModule} from '@angular/core';
import {StoreModule} from '@ngrx/store';
import {NotificationComponent} from './notification.component';


@NgModule({
  // Effects imported in main
  imports: [
    CommonModule,
    StoreModule.forFeature(featureKey, reducer),
  ],
  declarations: [
    NotificationComponent,
  ],
  exports: [
    NotificationComponent,
  ],
})

export class NotificationModule {
}
