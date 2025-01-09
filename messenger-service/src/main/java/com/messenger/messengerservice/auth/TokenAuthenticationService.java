package com.messenger.messengerservice.auth;

import com.messenger.messengerservice.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

@Service
public class TokenAuthenticationService {

    private static final String AUTH_HEADER_NAME = "Authorization";
    private static final String AUTH_HEADER_PREFIX = "Bearer ";

    private final TokenHandler tokenHandler;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public TokenAuthenticationService(TokenHandler tokenHandler,
                                      UserDetailsServiceImpl userDetailsServiceImpl) {
        this.tokenHandler = tokenHandler;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    public void addAuthentication(HttpServletResponse response, UserDetails userDetails) {
        String token = this.tokenHandler.generateTokenForUser(userDetails);
        String header = AUTH_HEADER_PREFIX + token;

        response.addHeader(AUTH_HEADER_NAME, header);
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        final String header = request.getHeader(AUTH_HEADER_NAME);

        if (header == null || !header.startsWith(AUTH_HEADER_PREFIX)) {
            throw new AccessDeniedException("No JWT token found in request headers");
        }

        String token = header.substring(AUTH_HEADER_PREFIX.length());
        UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(this.tokenHandler.extractUsername(token));

        if (userDetails == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            throw new AccessDeniedException("User is not authenticated");
        } else if (!this.tokenHandler.validateToken(token, userDetails)) {
            throw new AccessDeniedException("Token is not valid!");
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authToken;
    }

}
