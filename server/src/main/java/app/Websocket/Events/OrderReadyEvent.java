package app.Websocket.Events;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class OrderReadyEvent {
    private UUID orderId;
    private LocalDateTime readyAt;
    private Integer quantity;
    private Integer tableNo;
}
