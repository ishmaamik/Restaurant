package Orders.DTOs;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItemDTO {
   private UUID orderItemId;
   private UUID menuId;
   private String name;
   private BigDecimal unitPrice;
   private Integer quantity;
}
