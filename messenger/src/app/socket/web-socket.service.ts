import {Injectable} from "@angular/core";
import {Subject} from "rxjs";
import {Message} from "../model/message";
import {server} from "../../resources/server-settings";
import {Client, IFrame, IMessage, Stomp} from "@stomp/stompjs";
import {webSocket} from "../../resources/web-socket-settings";
import * as SockJS from "sockjs-client";
import {LocalStorage} from "../../resources/local-storage";

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {

  stompClient: Client | null = null;
  messageSubject: Subject<Message> = new Subject<Message>();

  connect() {
    const ws: WebSocket = new SockJS(this.getWebSocketURL());
    this.stompClient = Stomp.over(ws);

    this.stompClient.onConnect = (frame: IFrame) => {
      this.stompClient?.subscribe(webSocket.topic, (message: IMessage) => {
        this.messageSubject.next(JSON.parse(message.body));
      });
    };

    this.stompClient.onStompError = (frame: IFrame) => {
      console.error('Error occurred: ' + frame.headers['message']);
    };

    this.stompClient?.activate();
  };

  disconnect() {
    if (this.stompClient?.connected) {
      this.stompClient.deactivate();
    }
  }

  private getWebSocketURL(): string {
    return server.host + ':' + server.port + webSocket.webSocketSuffix
      + '?token=' + localStorage.getItem(LocalStorage.TOKEN);
  }
}
