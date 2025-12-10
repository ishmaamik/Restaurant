package Websocket.Events;

import Orders.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class KitchenOrderStatusEvent {
    private UUID orderId;
    private String triggeredBy;
    private LocalDateTime timestamp;
    private OrderStatus orderStatus;
    private Integer tableNo;
    private List<ItemSummary> itemsList;
}
