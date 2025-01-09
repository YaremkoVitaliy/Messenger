package com.messenger.messengerservice.mapper;

import com.messenger.messengerservice.dto.MessageDTO;
import com.messenger.messengerservice.model.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageDTO toDTO(Message entity) {
        MessageDTO dto = new MessageDTO();
        dto.setMessage(entity.getMessage());
        dto.setSendingDateTime(entity.getSendingDateTime());
        return dto;
    }

    public Message toEntity(MessageDTO dto) {
        Message entity = new Message();
        entity.setMessage(dto.getMessage());
        entity.setSendingDateTime(dto.getSendingDateTime());
        return entity;
    }
}
