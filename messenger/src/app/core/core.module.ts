import {NgModule, Optional, SkipSelf} from "@angular/core";
import {AuthModule} from "./auth/auth.module";
import {MainService} from "./services/main.service";
import {WebSocketService} from "./services/web-socket.service";

@NgModule({
  imports: [
    AuthModule
  ],
  providers: [
    MainService,
    WebSocketService
  ],
})
export class CoreModule {
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    if (parentModule) {
      throw new Error('CoreModule is already loaded. Import it in the AppModule only.');
    }
  }
}
