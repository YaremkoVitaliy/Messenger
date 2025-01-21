package com.messenger.messengerservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AuthRequestDTO {

    @NotNull
    @Size(min = 4, max = 50)
    private String username;
    @NotNull
    @Size(min = 4, max = 20)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
