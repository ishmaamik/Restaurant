package Orders.DTOs;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
   private UUID orderItemId;
   private UUID menuId;
   private String name;
   private BigDecimal unitPrice;
   private Integer quantity;
}
