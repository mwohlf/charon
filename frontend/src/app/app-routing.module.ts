import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ErrorComponent} from "./components/error/error.component";
import {HomeComponent} from "./components/home/home.component";
import {MainComponent} from "./components/main/main.component";
import {ProtectedComponent} from "./components/protected/protected.component";

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
  imports: [RouterModule.forRoot(routes,
    {
      scrollPositionRestoration: 'enabled', // one of 'disabled', 'enabled', 'top'
      enableTracing: false, // log to console for debugging
      errorHandler: routerErrorHandler,
      paramsInheritanceStrategy: 'always',
      relativeLinkResolution: 'corrected', // one of 'legacy', 'corrected'
    })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
