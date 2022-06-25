import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing.module';
import {AuthConfigModule} from './auth-config.module';
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

@NgModule({
  declarations: [
    AppComponent,
    ErrorComponent,
    HomeComponent,
    MainComponent,
    NavigationComponent,
  ],
  imports: [
    AppRoutingModule,
    AuthConfigModule,
    BrowserAnimationsModule,
    BrowserModule,
    LayoutModule,
    MatButtonModule,
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
