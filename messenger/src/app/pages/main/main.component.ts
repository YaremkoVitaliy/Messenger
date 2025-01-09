import {Component, OnInit} from '@angular/core';
import {MainService} from "./main.service";
import {Message} from "../../model/message";
import {AuthService} from "../../auth/auth.service";
import {catchError, filter, map, throwError} from "rxjs";
import {Router} from "@angular/router";
import {NbMenuService} from "@nebular/theme";
import {WebSocketService} from "../../socket/web-socket.service";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {

  contextMenu: { title: string }[] = [{title: 'Logout'}];
  username: string = 'Unknown';
  chatHistory?: Message[];

  constructor(private router: Router,
              private mainService: MainService,
              private authService: AuthService,
              private nbMenuService: NbMenuService,
              private webSocketService: WebSocketService) {
  }

  ngOnInit(): void {
    this.username = this.authService.getUsername();

    this.mainService.getChatHistory()
      .pipe(
        map(response => this.chatHistory = response),
        catchError(err => throwError(err))
      )
      .subscribe(() => {
        this.scrollToBottom();
      });

    this.nbMenuService.onItemClick()
      .pipe(
        filter(({tag}) => tag === 'my-context-menu'),
      )
      .subscribe(() => this.logout());

    this.webSocketService.messageSubject.asObservable()
      .subscribe(message => {
        if (message) {
          this.chatHistory?.push(message);
          this.scrollToBottom();
        }
      })
  }

  sendMessage(event: { message: string, files: File[] }): void {
    if (event.message.length > 200) {
      alert("Message length must be not greater than 200 characters!");
      return;
    }
    let message: Message = new Message(event.message, new Date(), this.username);
    this.mainService.sendMessage(message);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigateByUrl('/login')
      .then(() => this.webSocketService.disconnect());
  }

  scrollToBottom() {
    setTimeout(() => {
      window.scrollTo(0, document.body.scrollHeight);
    }, 0);
  }
}
