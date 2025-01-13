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

  username: string = '';
  password: string = '';
  message: string = '';

  constructor(private router: Router,
              private authService: AuthService,
              private webSocketService: WebSocketService) {
  }

  login(): void {
    this.message = 'Trying to log in ...';

    this.authService.login(this.username, this.password)
      .subscribe(data => {
        if (this.authService.isLoggedIn()) {
          this.message = 'Login is successful';
          this.router.navigateByUrl('/').then(() => this.webSocketService.connect());
        } else {
          this.message = 'Error occurred during login';
        }
      }, error => {
        this.message = 'Error occurred during login';
      }, () => {
        this.message = '';
      });
  }

  signUp(): void {
    this.message = 'Trying to sign up ...';

    this.authService.signUp(this.username, this.password)
      .subscribe(data => {
        if (this.authService.isLoggedIn()) {
          this.message = 'Sign up is successful';
          this.router.navigateByUrl('/').then(() => this.webSocketService.connect());
        } else {
          this.message = 'Error occurred during sign up';
        }
      }, error => {
        this.message = 'Error occurred during sign up';
      }, () => {
        this.message = '';
      });
  }
}
