package Orders.repository;

import Orders.domain.Order;
import Orders.enums.OrderStatus;
import Users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepo extends JpaRepository<Order, UUID> {
    List<Order> getOrdersByUser(UUID userId);

    List<Order> findOrdersByStatus(OrderStatus status);
}
