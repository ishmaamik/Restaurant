package Orders.DTOs;

import Orders.domain.OrderItem;
import Orders.enums.OrderType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderCashierDTO {
    private String orderId;
    private String paymentMethod;
    private OrderType orderType;
    private Integer tableNo;
    private BigDecimal subAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal serviceChargeAmount;
    private List<OrderItemDTO> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime preparingAt;
    private LocalDateTime readyAt;
    private LocalDateTime servedAt;
    private LocalDateTime paidAt;
    private LocalDateTime cancelledAt;
}
