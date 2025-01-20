package com.messenger.messengerservice.controller;

import com.messenger.messengerservice.auth.TokenHandler;
import com.messenger.messengerservice.dto.AuthRequestDTO;
import com.messenger.messengerservice.model.User;
import com.messenger.messengerservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenHandler tokenHandler;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager,
                          TokenHandler tokenHandler,
                          UserDetailsService userDetailsService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenHandler = tokenHandler;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> logIn(@RequestBody AuthRequestDTO authRequest) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(authRequest.getUsername());

        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        if (authentication.isAuthenticated()) {
            String token = this.tokenHandler.generateTokenForUser(userDetails);
            return ResponseEntity.ok().body(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody AuthRequestDTO authRequest) {
        User newUser = this.userService.createUser(authRequest);

        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        if (authentication.isAuthenticated()) {
            String token = this.tokenHandler.generateTokenForUser(newUser);
            return ResponseEntity.ok().body(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
