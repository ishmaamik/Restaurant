package app.Orders.mappers;

import app.Orders.DTOs.OrderItemDTO;
import app.Orders.domain.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "itemId", target = "orderItemId")
    @Mapping(source = "menu.menuId", target = "menuId")
    OrderItemDTO toOrderItemDTO(OrderItem orderItem);
}
