package com.messenger.messengerservice.service.impl;

import com.messenger.messengerservice.model.User;
import com.messenger.messengerservice.repository.UserRepository;
import com.messenger.messengerservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public User createUser(User user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(this.encoder.encode(user.getPassword()));
        return this.userRepository.save(newUser);
    }
}
