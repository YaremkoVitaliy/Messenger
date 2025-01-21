package com.messenger.messengerservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class MessageDTO {

    @NotNull
    @Size(min = 1, max = 200)
    private String message;
    @NotNull
    private LocalDateTime sendingDateTime;
    @NotNull
    @Size(min = 4, max = 50)
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
