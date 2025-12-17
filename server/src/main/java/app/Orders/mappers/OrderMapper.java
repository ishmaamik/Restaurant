package app.Orders.mappers;

import app.Orders.DTOs.CreateOrderDTO;
import app.Orders.DTOs.OrderCashierDTO;
import app.Orders.DTOs.OrderCustomerDTO;
import app.Orders.domain.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {
    OrderCustomerDTO toCustomerOrderDTO(Order order);

    @Mapping(source = "orderedBy.userId", target = "userId")
    CreateOrderDTO toCreateOrderDTO(Order order);

    OrderCashierDTO toCashierOrderDTO(Order order);

    List<OrderCustomerDTO> toCustomerDTOList(List<Order> orders);
}
