package app.Orders.DTOs;

import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDTO {
    private UUID userId;
    private Integer tableNo;
}
