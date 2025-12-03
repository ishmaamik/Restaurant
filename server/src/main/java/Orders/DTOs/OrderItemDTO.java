package Orders.DTOs;

import java.math.BigDecimal;
import java.util.UUID;


public class OrderItemDTO {
   private UUID orderItemId;
   private String name;
   private BigDecimal unitPrice;
   private Integer quantity;
}
