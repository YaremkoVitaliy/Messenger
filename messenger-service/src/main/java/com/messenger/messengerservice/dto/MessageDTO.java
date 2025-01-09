package com.messenger.messengerservice.dto;

import java.time.LocalDateTime;

public class MessageDTO {

    private String message;
    private LocalDateTime sendingDateTime;
    private String username;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSendingDateTime() {
        return sendingDateTime;
    }

    public void setSendingDateTime(LocalDateTime sendingDateTime) {
        this.sendingDateTime = sendingDateTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
