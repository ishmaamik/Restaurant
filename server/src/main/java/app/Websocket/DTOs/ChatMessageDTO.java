package app.Websocket.DTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessageDTO {
    private String sender;
    private String message;
    private String roomId;
    private long timestamp;
}