package Websocket.Config;

import Security.jwt.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private final JWTService jwtService;

    @Autowired
    public WebSocketAuthChannelInterceptor(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // safety check
        if (accessor == null) return message;

        // Authenticate ONLY on CONNECT
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            // Read Authorization header from STOMP connect frame
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Missing or invalid Authorization header for WebSocket");
            }

            String token = authHeader.substring(7);

            // 🔐 Validate JWT using your service
            if (!jwtService.isAccessTokenValid(token)) {
                throw new IllegalArgumentException("Expired or invalid JWT token");
            }

            // 🪪 Extract username from token
            String username = jwtService.extractUsernameFromAccessToken(token);

            // No roles stored in token for now → empty authority list
            List<SimpleGrantedAuthority> authorities = List.of();

            // Attach user (principal) to WebSocket session
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            accessor.setUser(auth); // now user is authenticated for WebSocket session
        }

        return message;
    }
}
