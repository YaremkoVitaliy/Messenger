import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {RouterOutlet} from "@angular/router";
import {AppRoutingModule} from "./app-routing.module";
import {LoginModule} from "./features/login/login.module";
import {HttpClientModule} from "@angular/common/http";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {JwtModule} from "@auth0/angular-jwt";
import {LocalStorage} from "./shared/constants/local-storage";
import {server} from "./shared/constants/server-settings";
import {MainModule} from "./features/main/main.module";
import {NbMenuModule, NbThemeModule} from "@nebular/theme";
import {CoreModule} from "./core/core.module";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    RouterOutlet,
    HttpClientModule,
    JwtModule.forRoot(
      {
        config: {
          tokenGetter: () => localStorage.getItem(LocalStorage.TOKEN),
          allowedDomains: [server.host + ":" + server.port],
          skipWhenExpired: true
        }
      }),
    BrowserAnimationsModule,
    AppRoutingModule,
    CoreModule,
    LoginModule,
    MainModule,
    NbThemeModule.forRoot(),
    NbMenuModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
