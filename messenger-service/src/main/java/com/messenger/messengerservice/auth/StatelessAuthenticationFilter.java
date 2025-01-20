package com.messenger.messengerservice.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class StatelessAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatelessAuthenticationFilter.class);
    private static final String AUTH_HEADER_NAME = "Authorization";
    private static final String AUTH_HEADER_PREFIX = "Bearer ";

    private final TokenHandler tokenHandler;
    private final UserDetailsService userDetailsService;

    @Autowired
    public StatelessAuthenticationFilter(TokenHandler tokenHandler,
                                         UserDetailsService userDetailsService) {
        this.tokenHandler = tokenHandler;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            final String header = request.getHeader(AUTH_HEADER_NAME);

            if (header == null || !header.startsWith(AUTH_HEADER_PREFIX)) {
                throw new AccessDeniedException("No JWT token found in request headers");
            }

            String token = header.substring(AUTH_HEADER_PREFIX.length());
            String username = this.tokenHandler.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (!this.tokenHandler.validateToken(token, userDetails)) {
                    throw new AccessDeniedException("Token is not valid!");
                }

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            filterChain.doFilter(request, response);
        }
    }
}
