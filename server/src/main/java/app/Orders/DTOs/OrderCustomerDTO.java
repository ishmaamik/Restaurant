package app.Orders.DTOs;
import lombok.*;
import app.Orders.enums.OrderType;

import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCustomerDTO {
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
    private UserWhoOrderedDTO orderedBy;
}
