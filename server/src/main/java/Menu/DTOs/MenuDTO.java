package Menu.DTOs;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class MenuDTO {

    private UUID menuId;
    private String name;
    private BigDecimal price;
    private boolean active;
    private String category;
}
