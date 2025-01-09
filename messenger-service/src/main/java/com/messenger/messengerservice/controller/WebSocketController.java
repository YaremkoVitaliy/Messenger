package com.messenger.messengerservice.controller;

import com.messenger.messengerservice.dto.MessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/message/send")
    @SendTo("/topic/messenger")
    public MessageDTO sendMessage(@Payload MessageDTO messageDTO) {
        return messageDTO;
    }
}
