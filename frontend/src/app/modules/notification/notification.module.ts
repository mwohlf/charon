import {CommonModule} from '@angular/common';
import {reducer} from './reducer';
import {NgModule} from '@angular/core';
import {StoreModule} from '@ngrx/store';
import {NotificationComponent} from './notification.component';
import {notificationFeature} from '../../shared/const';


@NgModule({
  // Effects imported in main
  imports: [
    CommonModule,
    StoreModule.forFeature(notificationFeature, reducer),
  ],
  declarations: [
    NotificationComponent,
  ],
  exports: [
    NotificationComponent,
  ],
})

// Module imported in main imports
export class NotificationModule {
}
