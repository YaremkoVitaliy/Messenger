import {NgModule} from "@angular/core";
import {AuthService} from "./auth.service";
import {JwtModule} from "@auth0/angular-jwt";
import {AuthInterceptor} from "./auth.interceptor";
import {HTTP_INTERCEPTORS} from "@angular/common/http";

@NgModule({
  imports: [
    JwtModule
  ],
  providers: [
    AuthService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
})
export class AuthModule {
}
