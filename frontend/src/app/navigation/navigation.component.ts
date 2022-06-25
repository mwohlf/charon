import {Component} from '@angular/core';
import {BreakpointObserver, Breakpoints} from '@angular/cdk/layout';
import {Observable} from 'rxjs';
import {map, shareReplay} from 'rxjs/operators';
import {ErrorComponent} from "../components/error/error.component";
import {HomeComponent} from "../components/home/home.component";
import {MainComponent} from "../components/main/main.component";
import {ProtectedComponent} from "../components/protected/protected.component";


@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent {

  prefix = '/';
  errorComponentPath = this.prefix + ErrorComponent.ROUTER_PATH;
  homeComponentPath = this.prefix + HomeComponent.ROUTER_PATH;
  mainComponentPath = this.prefix + MainComponent.ROUTER_PATH;
  protectedComponentPath = this.prefix + ProtectedComponent.ROUTER_PATH;

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  constructor(private breakpointObserver: BreakpointObserver) {
  }

}
