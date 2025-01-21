package com.messenger.messengerservice;

import com.messenger.messengerservice.dto.AuthRequestDTO;
import com.messenger.messengerservice.model.User;
import com.messenger.messengerservice.repository.UserRepository;
import com.messenger.messengerservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        AuthRequestDTO authRequest = new AuthRequestDTO();
        authRequest.setUsername("testUser");
        authRequest.setPassword("password123");

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setUsername("testUser");
        savedUser.setPassword("encodedPassword123");

        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword123");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.createUser(authRequest);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getUsername()).isEqualTo("testUser");
        assertThat(result.getPassword()).isEqualTo("encodedPassword123");

        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }
}
