import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing.module';
import {AuthConfigModule} from './app-auth.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {BrowserModule} from '@angular/platform-browser';
import {ErrorComponent} from './components/error/error.component';
import {HomeComponent} from './components/home/home.component';
import {LayoutModule} from '@angular/cdk/layout';
import {MainComponent} from './components/main/main.component';
import {NavigationComponent} from './navigation/navigation.component';
import {NgModule} from '@angular/core';
import {ProtectedComponent} from "./components/protected/protected.component";
import {ToolbarComponent} from "./navigation/toolbar/toolbar.component";
import {ApiModule, Configuration} from '../../build/generated';
import {StoreModule} from '@ngrx/store';

import {AppThemeModule} from "./app-theme.module";

@NgModule({
  declarations: [
    AppComponent,
    ErrorComponent,
    HomeComponent,
    MainComponent,
    NavigationComponent,
    ProtectedComponent,
    ToolbarComponent,
  ],
  imports: [
    ApiModule.forRoot(() => new Configuration({basePath: ''})),
    AppRoutingModule,
    AppThemeModule,
    AuthConfigModule,
    BrowserAnimationsModule,
    BrowserModule,
    LayoutModule,
    StoreModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
