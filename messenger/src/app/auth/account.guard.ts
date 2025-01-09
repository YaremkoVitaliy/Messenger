import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "./auth.service";
import {JwtHelperService} from "@auth0/angular-jwt";
import {LocalStorage} from "../../resources/local-storage";

export const accountGuard: CanActivateFn = (route, state) => {
  let authService = inject(AuthService);
  let router = inject(Router);
  let jwtHelperService = inject(JwtHelperService);

  const token = localStorage.getItem(LocalStorage.TOKEN);

  if (authService.isLoggedIn() && !jwtHelperService.isTokenExpired(token)) {
    router.navigate(['/']);
    return false;
  }

  return true;
};
