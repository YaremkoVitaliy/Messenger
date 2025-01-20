package com.messenger.messengerservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Table(name = "messages")
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, max = 200)
    private String message;

    @Column(name = "sending_date_time", nullable = false)
    private LocalDateTime sendingDateTime;

    @JoinColumn(name = "user_id",
            referencedColumnName = "user_id",
            updatable = false, nullable = false)
    @ManyToOne(targetEntity = User.class)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
