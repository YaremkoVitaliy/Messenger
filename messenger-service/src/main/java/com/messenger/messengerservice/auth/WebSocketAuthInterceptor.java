package com.messenger.messengerservice.auth;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private static final String TOKEN_PARAMETER = "token=";

    private final TokenHandler tokenHandler;

    public WebSocketAuthInterceptor(TokenHandler tokenHandler) {
        this.tokenHandler = tokenHandler;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        String query = request.getURI().getQuery();

        if (query != null && query.contains(TOKEN_PARAMETER)) {
            String token = query.substring(query.indexOf(TOKEN_PARAMETER) + 6);
            token = token.split("&")[0];

            return tokenHandler.validateToken(token);
        }

        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
    }
}
