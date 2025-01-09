package com.messenger.messengerservice.service.impl;

import com.messenger.messengerservice.dto.MessageDTO;
import com.messenger.messengerservice.mapper.MessageMapper;
import com.messenger.messengerservice.model.Message;
import com.messenger.messengerservice.repository.MessageRepository;
import com.messenger.messengerservice.repository.UserRepository;
import com.messenger.messengerservice.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageMapper mapper;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository,
                              UserRepository userRepository,
                              MessageMapper mapper) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public List<MessageDTO> getChatHistory() {
        return this.messageRepository.findLast50Messages().stream()
                .map(message -> {
                    MessageDTO dto = this.mapper.toDTO(message);
                    dto.setUsername(message.getUser().getUsername());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public MessageDTO sendMessage(MessageDTO messageDTO) {
        Message message = this.mapper.toEntity(messageDTO);
        this.userRepository.findByUsername(messageDTO.getUsername())
                .ifPresentOrElse(message::setUser,
                        () -> {
                            throw new UsernameNotFoundException("User not found");
                        });

        Message saved = this.messageRepository.save(message);
        MessageDTO result = this.mapper.toDTO(saved);
        result.setUsername(saved.getUser().getUsername());
        return result;
    }
}
