import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing.module';
import {AuthConfigModule} from './app-auth.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {BrowserModule} from '@angular/platform-browser';
import {ErrorComponent} from './components/error/error.component';
import {HomeComponent} from './components/home/home.component';
import {LayoutModule} from '@angular/cdk/layout';
import {MainComponent} from './components/main/main.component';
import {NgModule} from '@angular/core';
import {ProtectedComponent} from "./components/protected/protected.component";
import {HeaderComponent} from "./header/header.component";
import {ApiModule, Configuration} from '../../build/generated';
import {StoreModule} from '@ngrx/store';

import {AppThemeModule} from "./app-theme.module";
import {ShellComponent} from "./shell/shell.component";
import {FlexLayoutModule} from '@angular/flex-layout';
import {FooterComponent} from "./footer/footer.component";
import {MenuComponent} from "./menu/menu.component";

@NgModule({
  declarations: [
    AppComponent,
    ErrorComponent,
    HomeComponent,
    MainComponent,
    ProtectedComponent,
    FooterComponent,
    HeaderComponent,
    MenuComponent,
    ShellComponent,
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
    FlexLayoutModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppShellModule {
}
