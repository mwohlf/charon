import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ErrorComponent} from '../../pages/error/error.component';
import {HomeComponent} from '../../pages/home/home.component';
import {MainComponent} from '../../pages/main/main.component';
import {ProtectedComponent} from '../../pages/protected/protected.component';
import {LoggerHolder} from '../../shared/logger-holder';
import {LoginComponent} from '../../pages/login/login.component';
import {AutoLoginPartialRoutesGuard} from 'angular-auth-oidc-client';

const routes: Routes = [
  {
    path: LoginComponent.SPEC.route,
    component: LoginComponent,
    canActivate: [AutoLoginPartialRoutesGuard],
  },
  {
    path: ErrorComponent.SPEC.route,
    component: ErrorComponent,
  },
  {
    path: HomeComponent.SPEC.route,
    component: HomeComponent,
  },
  {
    path: MainComponent.SPEC.route,
    component: MainComponent,
  },
  {
    path: ProtectedComponent.SPEC.route,
    component: ProtectedComponent,
    canActivate: [AutoLoginPartialRoutesGuard],
  },
  {path: '', redirectTo: HomeComponent.SPEC.route, pathMatch: 'full'},
  {path: '**', redirectTo: ErrorComponent.SPEC.route},
];


export function routerErrorHandler(error: Error): void {
  LoggerHolder.logger.error('<routerErrorHandler> router error: ', JSON.stringify(error));
}

@NgModule({
  imports: [
    RouterModule.forRoot(routes,
      {
        scrollPositionRestoration: 'enabled',
        anchorScrolling: 'enabled',
        enableTracing: false,
        errorHandler: routerErrorHandler,
        paramsInheritanceStrategy: 'always',
      })],
  exports: [RouterModule],
})
export class RoutingModule {
}
