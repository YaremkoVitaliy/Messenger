import {Injectable} from "@angular/core";
import {Subject} from "rxjs";
import {Message} from "../../shared/models/message";
import {server} from "../../shared/constants/server-settings";
import {Client, IFrame, IMessage, Stomp, StompSubscription} from "@stomp/stompjs";
import {webSocket} from "../../shared/constants/web-socket-settings";
import * as SockJS from "sockjs-client";
import {LocalStorage} from "../../shared/constants/local-storage";

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {

  private stompClient: Client | null = null;
  private stompSubscription: StompSubscription | undefined;
  public messageSubject: Subject<Message> = new Subject<Message>();

  public connect(): void {
    const ws: WebSocket = new SockJS(this.getWebSocketURL());
    this.stompClient = Stomp.over(ws);

    this.stompClient.onConnect = (frame: IFrame): void => {
      this.stompSubscription = this.stompClient?.subscribe(webSocket.topic, (message: IMessage): void => {
        this.messageSubject.next(JSON.parse(message.body));
      });
    };

    this.stompClient.onStompError = (frame: IFrame): void => {
      console.error('Error occurred: ' + frame.headers['message']);
    };

    this.stompClient?.activate();
  };

  public disconnect(): void {
    if (this.stompClient?.connected) {
      this.stompSubscription?.unsubscribe();
      this.stompClient.deactivate();
    }
  }

  private getWebSocketURL(): string {
    return server.host + ':' + server.port + webSocket.webSocketSuffix
      + '?token=' + localStorage.getItem(LocalStorage.TOKEN);
  }
}
