package Orders.mappers;

import Orders.DTOs.OrderCashierDTO;
import Orders.DTOs.OrderCustomerDTO;
import Orders.domain.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {
    OrderCustomerDTO toCustomerOrderDTO(Order order);

    OrderCashierDTO toCashierOrderDTO(Order order);

    List<OrderCustomerDTO> toCustomerDTOList(List<Order> orders);
}
