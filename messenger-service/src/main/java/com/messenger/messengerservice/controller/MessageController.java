package com.messenger.messengerservice.controller;

import com.messenger.messengerservice.dto.MessageDTO;
import com.messenger.messengerservice.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/message")
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public MessageController(MessageService messageService,
                             SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/history")
    public ResponseEntity<List<MessageDTO>> getChatHistory() {
        return ResponseEntity.ok().body(this.messageService.getChatHistory());
    }

    @PostMapping(value = "/send", consumes = "application/json")
    public ResponseEntity<Void> sendMessage(@Valid @RequestBody MessageDTO messageDTO) {
        MessageDTO sentMessage = this.messageService.sendMessage(messageDTO);
        this.messagingTemplate.convertAndSend("/topic/messenger", sentMessage);
        return ResponseEntity.ok().build();
    }
}
