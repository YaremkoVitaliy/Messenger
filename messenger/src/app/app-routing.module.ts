import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {authGuard} from "./auth/auth.guard";
import {accountGuard} from "./auth/account.guard";

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    canActivate: [authGuard],
    data: {
      preload: true,
      loadAfter: 3000
    },
    loadChildren: () => import('./pages/main/main.module').then(m => m.MainModule),
  },
  {
    path: 'login',
    loadChildren: () => import('./pages/login/login.module').then(m => m.LoginModule),
    canActivate: [accountGuard]
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
