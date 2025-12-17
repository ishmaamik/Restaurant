package app.Websocket.services;

import app.Orders.domain.Order;
import app.Orders.enums.OrderStatus;
import app.Websocket.Events.OrderKitchenEvent;
import app.Websocket.Events.OrderStatusEvent;
import app.Websocket.Events.OrderReadyEvent;
import app.Websocket.Events.ItemSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyStatusChange(Order order, String triggeredBy) {
        OrderStatus status = order.getOrderStatus();

        OrderStatusEvent event = OrderStatusEvent.builder()
                .orderId(order.getOrderId())
                .tableNo(order.getTableNo())
                .status(status)
                .timestamp(LocalDateTime.now())
                .triggeredBy(triggeredBy)
                .build();

        switch (status) {
            case CREATED -> send("/topic/table/" + order.getTableNo(), event);

            case CONFIRMED -> {
                // Kitchen gets full order details
                OrderKitchenEvent kitchenEvent = OrderKitchenEvent.builder()
                        .orderId(order.getOrderId())
                        .tableNo(order.getTableNo())
                        .itemsList(order.getItems().stream()
                                .map(item -> ItemSummary.builder()
                                        .itemId(item.getItemId())
                                        .name(item.getName())
                                        .quantity(item.getQuantity())
                                        .notes(item.getNotes())
                                        .build())
                                .collect(Collectors.toList()))
                        .confirmedAt(order.getConfirmedAt())
                        .build();

                send("/topic/kitchen/new-orders", kitchenEvent);
                send("/topic/admin/orders", event);
                sendToUser(order.getOrderedBy().getUsername(), "/queue/order-status", event);
                send("/topic/table/" + order.getTableNo(), event);
            }

            case PREPARING -> {
                send("/topic/kitchen/orders", event);
                send("/topic/waiters/orders", event);
                sendToUser(order.getOrderedBy().getUsername(), "/queue/order-status", event);
                send("/topic/table/" + order.getTableNo(), event);
            }

            case READY -> {
                OrderReadyEvent readyEvent = OrderReadyEvent.builder()
                        .orderId(order.getOrderId())
                        .tableNo(order.getTableNo())
                        .readyAt(order.getReadyAt())
                        .quantity(order.getItems().size())
                        .build();

                send("/topic/waiters/ready-orders", readyEvent);
                send("/topic/kitchen/orders", event);
                sendToUser(order.getOrderedBy().getUsername(), "/queue/order-status", event);
                send("/topic/table/" + order.getTableNo(), event);
            }

            case SERVED -> {
                send("/topic/admin/orders", event);
                send("/topic/cashier/pending-payments", event);
                send("/topic/waiters/orders", event);
                sendToUser(order.getOrderedBy().getUsername(), "/queue/order-status", event);
                send("/topic/table/" + order.getTableNo(), event);
            }

            case PAID -> {
                send("/topic/admin/orders", event);
                send("/topic/waiters/orders", event);
                sendToUser(order.getOrderedBy().getUsername(), "/queue/order-status", event);
                send("/topic/table/" + order.getTableNo(), event);
            }

            case CANCELLED -> {
                send("/topic/admin/orders", event);
                send("/topic/kitchen/orders", event);
                sendToUser(order.getOrderedBy().getUsername(), "/queue/order-status", event);
                send("/topic/table/" + order.getTableNo(), event);
            }

            case PAYMENT_FAILED -> {
                send("/topic/admin/orders", event);
                send("/topic/cashier/failed-payments", event);
                sendToUser(order.getOrderedBy().getUsername(), "/queue/payment-failed", event);
                send("/topic/table/" + order.getTableNo(), event);
            }
        }
    }

    private void send(String destination, Object payload) {
        messagingTemplate.convertAndSend(destination, payload);
    }

    private void sendToUser(String username, String destination, Object payload) {
        messagingTemplate.convertAndSendToUser(username, destination, payload);
    }
}