package Websocket.Config;

import Websocket.DTOs.ChatMessageDTO;
import Websocket.DTOs.SendMessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Client sends to: /app/chat.send
    @MessageMapping("/chat.send")
    public void sendMessage(@Payload SendMessageDTO msg, Authentication auth) {

        String username = auth.getName(); // extracted by your interceptor

        ChatMessageDTO response = ChatMessageDTO.builder()
                .sender(username)
                .message(msg.getMessage())
                .roomId(msg.getRoomId())
                .timestamp(Instant.now().toEpochMilli())
                .build();

        // Broadcast to topic for that room
        messagingTemplate.convertAndSend("/topic/room." + msg.getRoomId(), response);
    }
}

