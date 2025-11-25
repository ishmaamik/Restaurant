package Orders.repository;

import Orders.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderItemRepo extends JpaRepository<OrderItem, UUID> {
    List<OrderItem> findByOrder(UUID orderId);
}
