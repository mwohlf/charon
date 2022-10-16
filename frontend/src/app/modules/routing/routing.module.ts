import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ErrorComponent} from '../../pages/error/error.component';
import {HomeComponent} from '../../pages/home/home.component';
import {MainComponent} from '../../pages/main/main.component';
import {ProtectedComponent} from '../../pages/protected/protected.component';

const routes: Routes = [
  {
    path: ErrorComponent.ROUTER_PATH,
    component: ErrorComponent,
  },
  {
    path: HomeComponent.ROUTER_PATH,
    component: HomeComponent,
  },
  {
    path: MainComponent.ROUTER_PATH,
    component: MainComponent,
  },
  {
    path: ProtectedComponent.ROUTER_PATH,
    component: ProtectedComponent,
  },
  {path: '', redirectTo: HomeComponent.ROUTER_PATH, pathMatch: 'full'},
  {path: '**', redirectTo: ErrorComponent.ROUTER_PATH},
];


export function routerErrorHandler(error: Error): void {
  console.error('router error: ', JSON.stringify(error));
}

@NgModule({
  imports: [
    RouterModule.forRoot(routes,
      {
        scrollPositionRestoration: 'enabled', // one of 'disabled', 'enabled', 'top'
        anchorScrolling: 'enabled',
        relativeLinkResolution: 'corrected', // one of 'legacy', 'corrected'
        enableTracing: false, // log to console for debugging
        errorHandler: routerErrorHandler,
        paramsInheritanceStrategy: 'always',
      })],
  exports: [RouterModule],
})
export class RoutingModule {
}
