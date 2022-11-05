import {Component, OnDestroy, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from '../../app-shell.module';
import {
  MatSnackBar,
  MatSnackBarRef,
  TextOnlySnackBar,
} from '@angular/material/snack-bar';
import {selectNotificationQueue} from './selector';
import {NotificationData} from './reducer';
import {confirmNotification} from './action';

@Component({
  selector: 'app-notification',
  template: '',
})
export class NotificationComponent implements OnInit, OnDestroy {

  private subscription;
  private handle: MatSnackBarRef<TextOnlySnackBar> | null = null;

  constructor(
    public store: Store<AppState>,
    private matSnackBar: MatSnackBar,
  ) {
    this.subscription = this.store.select(selectNotificationQueue)
      .subscribe((next: NotificationData[]) => {
        if (next.length == 0) {
          matSnackBar.dismiss();
        } else {
          if (this.handle == null) {
            this.openSnackbar(next[0]);
          }
        }
      });
  }

  private openSnackbar(next: NotificationData): void {
    this.handle = this.matSnackBar.open(
      next.message, 'OK', {
        horizontalPosition: 'center',
        verticalPosition: 'bottom',
      },
    );

    this.handle.afterDismissed().subscribe(() => {
      this.handle = null;
      this.store.dispatch(confirmNotification());
    });
  }

  ngOnInit(): void {
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

}
