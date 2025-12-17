package app.Websocket.DTOs;

import lombok.Data;

@Data
public class SendMessageDTO {
    private String message;
    private String roomId;
}
