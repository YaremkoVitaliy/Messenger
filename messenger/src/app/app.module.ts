import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {RouterOutlet} from "@angular/router";
import {AppRoutingModule} from "./app-routing.module";
import {LoginModule} from "./pages/login/login.module";
import {HttpClientModule} from "@angular/common/http";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {JwtModule} from "@auth0/angular-jwt";
import {AuthModule} from "./auth/auth.module";
import {LocalStorage} from "../resources/local-storage";
import {server} from "../resources/server-settings";
import {MainModule} from "./pages/main/main.module";
import {NbMenuModule, NbThemeModule} from "@nebular/theme";
import {WebSocketService} from "./socket/web-socket.service";

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
    AuthModule,
    LoginModule,
    MainModule,
    NbThemeModule.forRoot(),
    NbMenuModule.forRoot()
  ],
  providers: [WebSocketService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
