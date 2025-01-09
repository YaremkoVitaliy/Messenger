import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "./auth.service";
import {JwtHelperService} from "@auth0/angular-jwt";
import {LocalStorage} from "../../resources/local-storage";

export const authGuard: CanActivateFn = (route, state) => {
  let authService: AuthService = inject(AuthService);
  let router: Router = inject(Router);
  let jwtHelperService: JwtHelperService = inject(JwtHelperService);

  const token: string | null = localStorage.getItem(LocalStorage.TOKEN);

  if (authService.isLoggedIn() && !jwtHelperService.isTokenExpired(token)) {
    return true;
  }

  router.navigate(['/login']);
  return false;
};
