package Orders.mappers;

import Orders.DTOs.OrderItemDTO;
import Orders.domain.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemDTO toOrderItemDTO(OrderItem orderItem);
}
