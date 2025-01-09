package com.messenger.messengerservice.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Objects;

public class StatelessAuthenticationFilter extends GenericFilterBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(StatelessAuthenticationFilter.class);

    private final TokenAuthenticationService tokenAuthenticationService;

    public StatelessAuthenticationFilter(TokenAuthenticationService tokenAuthenticationService) {
        this.tokenAuthenticationService = Objects.requireNonNull(tokenAuthenticationService);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        try {
            Authentication authentication = this.tokenAuthenticationService.getAuthentication((HttpServletRequest) request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (AccessDeniedException e) {
            LOGGER.error(e.getMessage());
            filterChain.doFilter(request, response);
        }
    }

}
