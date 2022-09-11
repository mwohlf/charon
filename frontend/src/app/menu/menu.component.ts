import {Component, OnInit} from '@angular/core';
import {ErrorComponent} from '../components/error/error.component';
import {HomeComponent} from '../components/home/home.component';
import {MainComponent} from '../components/main/main.component';
import {ProtectedComponent} from '../components/protected/protected.component';
import {Observable} from 'rxjs';
import {BreakpointObserver, Breakpoints} from '@angular/cdk/layout';
import {map, shareReplay} from 'rxjs/operators';
import {Store} from '@ngrx/store';
import {AppState} from '../app-shell.module';

@Component({
  selector: '.app-menu',
  templateUrl: './menu.component.html',
})
export class MenuComponent implements OnInit {

  prefix = '/';

  public menuItems = [
    {
      'route': this.prefix + ErrorComponent.ROUTER_PATH,
      'icon': 'home',
      'title': 'Error',
    },
    {
      'route': this.prefix + HomeComponent.ROUTER_PATH,
      'icon': 'home',
      'title': 'Home',
    },
    {
      'route': this.prefix + MainComponent.ROUTER_PATH,
      'icon': 'home',
      'title': 'Main',
    },
    {
      'route': this.prefix + ProtectedComponent.ROUTER_PATH,
      'icon': 'home',
      'title': 'Protected',
    },
  ];

  constructor(
    public store: Store<AppState>,
  ) {
  }


  ngOnInit(): void {
  }

  menuItemClick() {

  }

}
