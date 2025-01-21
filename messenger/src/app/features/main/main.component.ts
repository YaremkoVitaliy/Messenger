import {Component, OnDestroy, OnInit} from '@angular/core';
import {MainService} from "../../core/services/main.service";
import {Message} from "../../shared/models/message";
import {AuthService} from "../../core/auth/auth.service";
import {catchError, filter, map, Subscription, throwError} from "rxjs";
import {Router} from "@angular/router";
import {NbMenuService} from "@nebular/theme";
import {WebSocketService} from "../../core/services/web-socket.service";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit, OnDestroy {

  public contextMenu: { title: string }[] = [{title: 'Logout'}];
  public username: string = 'Unknown';
  public chatHistory?: Message[];
  private chatHistorySubscription?: Subscription;
  private nbMenuSubscription?: Subscription;
  private webSocketSubscription?: Subscription;
  private messageSubscription?: Subscription;

  constructor(private router: Router,
              private mainService: MainService,
              private authService: AuthService,
              private nbMenuService: NbMenuService,
              private webSocketService: WebSocketService) {
  }

  ngOnInit(): void {
    this.username = this.authService.getUsername();

    this.chatHistorySubscription = this.mainService.getChatHistory()
      .pipe(
        map(response => this.chatHistory = response),
        catchError(err => throwError(() => err))
      )
      .subscribe((): void => {
        this.scrollToBottom();
      });

    this.nbMenuSubscription = this.nbMenuService.onItemClick()
      .pipe(
        filter(({tag}) => tag === 'my-context-menu'),
      )
      .subscribe(() => this.logout());

    this.webSocketSubscription = this.webSocketService.messageSubject.asObservable()
      .subscribe(message => {
        if (message) {
          this.chatHistory?.push(message);
          this.scrollToBottom();
        }
      })
  }

  ngOnDestroy(): void {
    this.chatHistorySubscription?.unsubscribe();
    this.nbMenuSubscription?.unsubscribe();
    this.webSocketSubscription?.unsubscribe();
    this.messageSubscription?.unsubscribe();
  }

  public sendMessage(event: { message: string, files: File[] }): void {
    if (event.message.length > 200) {
      alert("Message length must be not greater than 200 characters!");
      return;
    }
    let message: Message = new Message(event.message, new Date(), this.username);
    this.messageSubscription = this.mainService.sendMessage(message);
  }

  private logout(): void {
    this.authService.logout();
    this.router.navigateByUrl('/login')
      .then(() => this.webSocketService.disconnect());
  }

  private scrollToBottom(): void {
    setTimeout((): void => {
      window.scrollTo(0, document.body.scrollHeight);
    }, 0);
  }
}
