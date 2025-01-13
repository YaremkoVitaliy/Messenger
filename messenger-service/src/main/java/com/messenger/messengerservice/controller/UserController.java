package com.messenger.messengerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messenger.messengerservice.auth.TokenHandler;
import com.messenger.messengerservice.model.User;
import com.messenger.messengerservice.service.UserService;
import com.messenger.messengerservice.utils.Base64DecoderUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenHandler tokenHandler;

    @Autowired
    public UserController(UserService userService,
                          AuthenticationManager authenticationManager,
                          TokenHandler tokenHandler) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenHandler = tokenHandler;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(HttpServletRequest request) throws IOException {
        byte[] authData = Base64DecoderUtils.decodeBase64(request.getInputStream());
        User user = new ObjectMapper().readValue(authData, User.class);

        this.userService.createUser(user);

        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        HttpHeaders headers = new HttpHeaders();
        if (authentication.isAuthenticated()) {
            String token = this.tokenHandler.generateTokenForUser(user);
            headers.setBearerAuth(token);
        }
        return ResponseEntity.ok().headers(headers).build();
    }
}
