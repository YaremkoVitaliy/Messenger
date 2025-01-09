export class Message {

  message: string;
  sendingDateTime: Date;
  username: string;

  constructor(message: string,
              sendingDateTime: Date,
              username: string) {
    this.message = message;
    this.sendingDateTime = sendingDateTime;
    this.username = username;
  }
}
