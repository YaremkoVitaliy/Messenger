package com.messenger.messengerservice;

import com.messenger.messengerservice.dto.MessageDTO;
import com.messenger.messengerservice.mapper.MessageMapper;
import com.messenger.messengerservice.model.Message;
import com.messenger.messengerservice.model.User;
import com.messenger.messengerservice.repository.MessageRepository;
import com.messenger.messengerservice.repository.UserRepository;
import com.messenger.messengerservice.service.impl.MessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class MessageServiceImplTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageMapper mapper;

    @InjectMocks
    private MessageServiceImpl messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnChatHistory() {
        Message message1 = new Message();
        User user1 = new User();
        user1.setUsername("user1");
        message1.setUser(user1);

        MessageDTO messageDTO1 = new MessageDTO();
        messageDTO1.setMessage("Hello!");
        messageDTO1.setUsername("user1");

        when(messageRepository.findLast50Messages()).thenReturn(List.of(message1));
        when(mapper.toDTO(message1)).thenReturn(messageDTO1);

        List<MessageDTO> result = messageService.getChatHistory();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getMessage()).isEqualTo("Hello!");
        assertThat(result.getFirst().getUsername()).isEqualTo("user1");

        verify(messageRepository).findLast50Messages();
        verify(mapper).toDTO(message1);
    }

    @Test
    void shouldSendMessageSuccessfully() {
        MessageDTO inputDTO = new MessageDTO();
        inputDTO.setUsername("user1");
        inputDTO.setMessage("Hello!");

        User user = new User();
        user.setUsername("user1");

        Message message = new Message();
        message.setUser(user);
        message.setMessage("Hello!");

        Message savedMessage = new Message();
        savedMessage.setUser(user);
        savedMessage.setMessage("Hello!");

        MessageDTO outputDTO = new MessageDTO();
        outputDTO.setUsername("user1");
        outputDTO.setMessage("Hello!");

        when(mapper.toEntity(inputDTO)).thenReturn(message);
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        when(messageRepository.save(message)).thenReturn(savedMessage);
        when(mapper.toDTO(savedMessage)).thenReturn(outputDTO);

        MessageDTO result = messageService.sendMessage(inputDTO);

        assertThat(result.getUsername()).isEqualTo("user1");
        assertThat(result.getMessage()).isEqualTo("Hello!");

        verify(mapper).toEntity(inputDTO);
        verify(userRepository).findByUsername("user1");
        verify(messageRepository).save(message);
        verify(mapper).toDTO(savedMessage);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundWhileSendingMessage() {
        MessageDTO inputDTO = new MessageDTO();
        inputDTO.setUsername("unknownUser");

        Message message = new Message();
        when(mapper.toEntity(inputDTO)).thenReturn(message);
        when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.sendMessage(inputDTO))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");

        verify(mapper).toEntity(inputDTO);
        verify(userRepository).findByUsername("unknownUser");
        verifyNoInteractions(messageRepository);
    }
}
