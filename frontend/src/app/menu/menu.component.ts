import {Component, OnInit} from '@angular/core';
import {ErrorComponent} from '../components/error/error.component';
import {HomeComponent} from '../components/home/home.component';
import {MainComponent} from '../components/main/main.component';
import {ProtectedComponent} from '../components/protected/protected.component';
import {Observable} from 'rxjs';
import {BreakpointObserver, Breakpoints} from '@angular/cdk/layout';
import {map, shareReplay} from 'rxjs/operators';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
})
export class MenuComponent implements OnInit {

  prefix = '/';
  errorComponentPath = this.prefix + ErrorComponent.ROUTER_PATH;
  homeComponentPath = this.prefix + HomeComponent.ROUTER_PATH;
  mainComponentPath = this.prefix + MainComponent.ROUTER_PATH;
  protectedComponentPath = this.prefix + ProtectedComponent.ROUTER_PATH;

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay(),
    );

  constructor(private breakpointObserver: BreakpointObserver) {
  }


  ngOnInit(): void {
  }

}
