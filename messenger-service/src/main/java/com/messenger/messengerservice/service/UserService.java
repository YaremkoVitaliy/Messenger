package com.messenger.messengerservice.service;

import com.messenger.messengerservice.dto.AuthRequestDTO;
import com.messenger.messengerservice.model.User;

public interface UserService {

    User createUser(AuthRequestDTO authRequest);
}
