import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../../auth/auth.service";
import {WebSocketService} from "../../socket/web-socket.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  public username: string = '';
  public password: string = '';
  public message: string = '';

  constructor(private router: Router,
              private authService: AuthService,
              private webSocketService: WebSocketService) {
  }

  public login(): void {
    this.message = 'Trying to log in ...';

    this.authService.login(this.username, this.password)
      .subscribe({
        next: _ => {
          this.message = 'Login is successful';
          this.router.navigateByUrl('/').then(() => this.webSocketService.connect());
        },
        error: (): void => { this.message = 'Error occurred during login'; }
      });
  }

  public signUp(): void {
    this.message = 'Trying to sign up ...';

    this.authService.signUp(this.username, this.password)
      .subscribe({
        next: _ => {
          this.message = 'Sign up is successful';
          this.router.navigateByUrl('/').then(() => this.webSocketService.connect());
        },
        error: (): void => { this.message = 'Error occurred during sign up'; }
      });
  }
}
