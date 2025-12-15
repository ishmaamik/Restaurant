package Orders.repository;

import Orders.domain.Order;
import Orders.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepo extends JpaRepository<Order, UUID>{
    Optional<List<Order>> findByOrderedBy_UserId(UUID userId);

    Optional<List<Order>> getOrdersByStatus(OrderStatus status);
}
