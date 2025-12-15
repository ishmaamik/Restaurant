package Orders.mappers;

import Orders.DTOs.OrderItemDTO;
import Orders.domain.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "itemId", target = "orderItemId")
    @Mapping(source = "menu.menuId", target = "menuId")
    OrderItemDTO toOrderItemDTO(OrderItem orderItem);
}
