import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {JwtHelperService} from '@auth0/angular-jwt';
import {tap} from 'rxjs/operators';
import {Observable} from 'rxjs';
import {server} from "../../resources/server-settings";
import {LocalStorage} from "../../resources/local-storage";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly UNKNOWN_USER: string = 'Unknown';

  constructor(private http: HttpClient,
              private jwtHelper: JwtHelperService) {
  }

  public login(username: string, password: string): Observable<String> {
    return this.authenticate(username, password, server.loginEndpoint);
  }

  public signUp(username: string, password: string): Observable<String> {
    return this.authenticate(username, password, server.signUpEndpoint);
  }

  public logout(): void {
    localStorage.removeItem(LocalStorage.TOKEN);
  }

  public isLoggedIn(): boolean {
    return !!localStorage.getItem(LocalStorage.TOKEN);
  }

  public getUsername(): string {
    const token: string | null = localStorage.getItem(LocalStorage.TOKEN);
    if (token == null) {
      return this.UNKNOWN_USER;
    }
    const username = this.jwtHelper.decodeToken(token).sub;
    return username != null ? username : this.UNKNOWN_USER;
  }

  private authenticate(username: string, password: string, endpoint: string): Observable<String> {
    const headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.http.post<String>(this.getUrlOrigin() + endpoint,
      JSON.stringify({
        username, password
      }),
      {
        responseType: 'text' as 'json',
        headers
      })
      .pipe(
        tap((token: String): void => {
            if (token != null) {
              localStorage.setItem(LocalStorage.TOKEN, token.toString());
            }
          }
        ));
  }

  private getUrlOrigin(): string {
    return server.host + ':' + server.port;
  }
}
