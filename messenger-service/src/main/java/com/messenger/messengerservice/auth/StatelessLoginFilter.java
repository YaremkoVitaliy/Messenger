package com.messenger.messengerservice.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messenger.messengerservice.model.User;
import com.messenger.messengerservice.service.impl.UserDetailsServiceImpl;
import com.messenger.messengerservice.utils.Base64DecoderUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Configurable
public class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final TokenAuthenticationService tokenAuthenticationService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public StatelessLoginFilter(String urlMapping,
                                TokenAuthenticationService tokenAuthenticationService,
                                UserDetailsServiceImpl userDetailsServiceImpl,
                                AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(urlMapping));
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.tokenAuthenticationService = tokenAuthenticationService;

        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {

        if (HttpMethod.POST.name().equals(request.getMethod())) {
            byte[] authData = Base64DecoderUtils.decodeBase64(request.getInputStream());

            UserDetails user = new ObjectMapper().readValue(authData, User.class);
            UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(
                    user.getUsername(), user.getPassword());

            return getAuthenticationManager().authenticate(loginToken);
        } else {
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) {

        UserDetails authenticatedUser = this.userDetailsServiceImpl.loadUserByUsername(authentication.getName());
        this.tokenAuthenticationService.addAuthentication(response, authenticatedUser);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
