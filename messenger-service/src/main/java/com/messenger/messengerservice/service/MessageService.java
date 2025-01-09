package com.messenger.messengerservice.service;

import com.messenger.messengerservice.dto.MessageDTO;

import java.util.List;

public interface MessageService {

    List<MessageDTO> getChatHistory();

    MessageDTO sendMessage(MessageDTO messageDTO);
}
