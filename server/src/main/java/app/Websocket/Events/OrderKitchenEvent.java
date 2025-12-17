package app.Websocket.Events;

import app.Orders.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class OrderKitchenEvent {
    private UUID orderId;
    private String triggeredBy;
    private LocalDateTime confirmedAt;
    private OrderStatus orderStatus;
    private Integer tableNo;
    private List<ItemSummary> itemsList;
}
