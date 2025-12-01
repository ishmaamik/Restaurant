package Orders.mappers;

import Orders.DTOs.OrderCashierDTO;
import Orders.DTOs.OrderCustomerDTO;
import Users.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderCustomerDTO mapperOrderCustomer( User user);

    OrderCashierDTO mapperOrderCashier(User user);
}
