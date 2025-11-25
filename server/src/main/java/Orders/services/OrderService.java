package Orders.services;

import Menu.domain.Menu;
import Menu.repository.MenuRepo;
import Menu.services.MenuService;
import Orders.domain.Order;
import Orders.domain.OrderItem;
import Orders.enums.ItemStatus;
import Orders.enums.OrderStatus;
import Orders.repository.OrderRepo;
import Payment.enums.PaymentStatus;
import Users.domain.User;
import Users.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepo orderRepo;
    private MenuService menuService;
    private UserService userService;

    public Order createOrder(UUID userId, Integer tableNo){
        User user= userService.getUserByUserId(userId);
        Order order= Order.builder()
                .orderedBy(user)
                .orderStatus(OrderStatus.CREATED)
                .tableNo(tableNo)
                .paymentStatus(PaymentStatus.PENDING)
                .taxAmount(BigDecimal.ZERO)
                .serviceChargeAmount(BigDecimal.ZERO)
                .totalAmount(BigDecimal.ZERO)
                .build();
        return orderRepo.save(order);
    }

    public Order getOrder(UUID orderId){
       return orderRepo.findById(orderId)
                .orElseThrow(()->new EntityNotFoundException("Order not found"));
    }

    public Order addItem(UUID orderId, UUID menuId){
        Order order= getOrder(orderId);
        Menu menu= menuService.getMenu(menuId);

        OrderItem orderItem= OrderItem.builder()
                .menu(menu)
                .order(order)
                .quantity(1)
                .unitPrice(menu.getPrice())
                .totalPrice(menu.getPrice())
                .itemStatus(ItemStatus.PENDING)
                        .build();



        order.addItem(orderItem);
        return orderRepo.save(order);
    }

    public Order removeItem(UUID orderId, UUID itemId){
        Order order= getOrder(orderId);

        OrderItem orderItem= order
                .getItems()
                .stream()
                .filter(it-> it.getItemId().equals(itemId))
                .findFirst()    //Matches the first out of many if many exists
                .orElseThrow(()->new EntityNotFoundException("Not Found item"))

        order.removeItem(orderItem);
        return orderRepo.save(order);
    }

    public Order changeStatus(UUID orderId, OrderStatus orderStatus){
        Order order= getOrder(orderId);

        order.setOrderStatus(orderStatus);

        return orderRepo.save(order);
    }
}
