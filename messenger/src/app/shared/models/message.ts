export class Message {

  public message: string;
  public sendingDateTime: Date;
  public username: string;

  constructor(message: string,
              sendingDateTime: Date,
              username: string) {
    this.message = message;
    this.sendingDateTime = sendingDateTime;
    this.username = username;
  }
}
