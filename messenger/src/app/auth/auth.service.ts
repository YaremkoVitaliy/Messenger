import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
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

  public login(username: string, password: string): Observable<HttpResponse<Object>> {
    return this.authenticate(username, password, server.loginEndpoint);
  }

  public signUp(username: string, password: string): Observable<HttpResponse<Object>> {
    return this.authenticate(username, password, server.signUpEndpoint);
  }

  private authenticate(username: string, password: string, endpoint: string): Observable<HttpResponse<Object>> {
    const headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.http.post<HttpResponse<Object>>(this.getUrlOrigin() + endpoint,
      btoa(JSON.stringify({
        username, password
      })),
      {
        observe: 'response',
        headers
      })
      .pipe(
        tap((data: HttpResponse<Object>) => {
            const authHeader: string | null = data.headers.get('Authorization');
            if (authHeader != null) {
              localStorage.setItem(LocalStorage.TOKEN, authHeader.substring(7));
            }
          }
        ));
  }

  private getUrlOrigin(): string {
    return server.host + ':' + server.port;
  }

  logout(): void {
    localStorage.removeItem(LocalStorage.TOKEN);
  }

  isLoggedIn(): boolean {
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
}
