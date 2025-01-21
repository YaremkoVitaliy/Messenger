import {Component, OnDestroy} from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../../core/auth/auth.service";
import {WebSocketService} from "../../core/services/web-socket.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnDestroy {

  public username: string = '';
  public password: string = '';
  public message: string = '';
  private loginSubscription?: Subscription;
  private signUpSubscription?: Subscription;

  constructor(private router: Router,
              private authService: AuthService,
              private webSocketService: WebSocketService) {
  }

  ngOnDestroy(): void {
    this.loginSubscription?.unsubscribe();
    this.signUpSubscription?.unsubscribe();
  }

  public login(): void {
    this.message = 'Trying to log in ...';

    this.loginSubscription = this.authService.login(this.username, this.password)
      .subscribe({
        next: _ => {
          this.message = 'Login is successful';
          this.router.navigateByUrl('/').then(() => this.webSocketService.connect());
        },
        error: (): void => {
          this.message = 'Error occurred during login';
        }
      });
  }

  public signUp(): void {
    this.message = 'Trying to sign up ...';

    this.signUpSubscription = this.authService.signUp(this.username, this.password)
      .subscribe({
        next: _ => {
          this.message = 'Sign up is successful';
          this.router.navigateByUrl('/').then(() => this.webSocketService.connect());
        },
        error: (): void => {
          this.message = 'Error occurred during sign up';
        }
      });
  }
}
