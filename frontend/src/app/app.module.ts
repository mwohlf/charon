import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing.module';
import {AuthConfigModule} from './app-auth.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {BrowserModule} from '@angular/platform-browser';
import {ErrorComponent} from './components/error/error.component';
import {HomeComponent} from './components/home/home.component';
import {LayoutModule} from '@angular/cdk/layout';
import {MainComponent} from './components/main/main.component';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatListModule} from '@angular/material/list';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatToolbarModule} from '@angular/material/toolbar';
import {NavigationComponent} from './navigation/navigation.component';
import {NgModule} from '@angular/core';
import {ProtectedComponent} from "./components/protected/protected.component";
import {ToolbarComponent} from "./navigation/toolbar/toolbar.component";
import {ApiModule, Configuration } from '../../build/generated';
import {MatCardModule} from '@angular/material/card';

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
    AuthConfigModule,
    BrowserAnimationsModule,
    BrowserModule,
    LayoutModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatListModule,
    MatSidenavModule,
    MatToolbarModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
