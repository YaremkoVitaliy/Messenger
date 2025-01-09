import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, map, throwError} from "rxjs";
import {Message} from "../../model/message";
import {server} from "../../../resources/server-settings";

@Injectable({
  providedIn: 'root'
})
export class MainService {

  constructor(protected http: HttpClient) {
  }

  public getChatHistory() {
    return this.http.get<Message[]>(this.getMessageApiURL() + '/history');
  }

  public sendMessage(message: Message) {
    let headers: HttpHeaders = new HttpHeaders();
    headers = headers.append('Content-Type', 'application/json');
    headers = headers.append('Accept', 'application/json');

    return this.http.post<Message>(this.getMessageApiURL() + '/send',
      JSON.stringify(message),
      {
        observe: 'response',
        headers: headers
      })
      .pipe(
        map(response => response),
        catchError(err => throwError(err))
      )
      .subscribe({
        error: (error) => {
          console.log('Error occurred while trying to send message.', error);
          alert('Message wasn\'t sent due to error: ' + error)
        }
      });
  }

  private getMessageApiURL(): string {
    return server.host + ':' + server.port + server.messageEndpoint;
  }
}
