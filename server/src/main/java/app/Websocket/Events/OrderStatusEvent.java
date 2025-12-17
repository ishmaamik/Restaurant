package app.Websocket.Events;

import app.Orders.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class OrderStatusEvent {
    private UUID orderId;
    private Integer tableNo;
    private String triggeredBy;
    private OrderStatus status;
    private LocalDateTime timestamp;
}
