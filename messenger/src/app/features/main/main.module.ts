import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {MainComponent} from "./main.component";
import {MainRoutingModule} from "./main-routing.module";
import {FormsModule} from "@angular/forms";
import {NbChatModule, NbContextMenuModule, NbLayoutModule, NbUserModule} from "@nebular/theme";

@NgModule({
  imports: [
    CommonModule,
    MainRoutingModule,
    FormsModule,
    NbChatModule,
    NbLayoutModule,
    NbContextMenuModule,
    NbUserModule
  ],
  providers: [],
  declarations: [MainComponent]
})
export class MainModule {
}
