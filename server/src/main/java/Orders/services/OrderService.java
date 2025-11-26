package Orders.services;

import Menu.domain.Menu;
import Menu.services.MenuService;
import Orders.domain.Order;
import Orders.domain.OrderItem;
import Orders.enums.ItemStatus;
import Orders.enums.OrderStatus;
import Orders.repository.OrderRepo;
import Payment.enums.PaymentStatus;
import Users.domain.User;
import Users.enums.UserRole;
import Users.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepo orderRepo;
    private final MenuService menuService;
    private final UserService userService;

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
                .orElseThrow(()->new EntityNotFoundException("Not Found item"));

        order.removeItem(orderItem);
        return orderRepo.save(order);
    }

    public Order changeStatus(UUID orderId, OrderStatus orderStatus){
        Order order= getOrder(orderId);

        order.setOrderStatus(orderStatus);

        return orderRepo.save(order);
    }

    public Order confirmOrder(UUID orderId){
        //Authorization check is only for the controllers
        //Business logic for the service
        //Enforcing business rules for the entity itself
        Order order= getOrder(orderId);
        order.confirm();
        save(order);
        return order;
    }

    public Order prepareOrder(UUID orderId, User user){
        Order order= getOrder(orderId);
        order.markPreparing();
        save(order);
        return order;
    }

    public Order readyOrder(UUID orderId, User user){
        Order order= getOrder(orderId);
        order.markReady();
        save(order);
        return order;
    }

    public Order serveOrder(UUID orderId, User user){
        Order order= getOrder(orderId);
        order.markServed();
        save(order);
        return order;
    }

    public Order paidOrder(UUID orderId, User user){
        Order order= getOrder(orderId);
        order.markPaid();
        save(order);
        return order;
    }

    public Order cancelOrder(UUID orderId, User user){
        Order order= getOrder(orderId);
        order.cancel();
        save(order);
        return order;
    }

    public Order save(Order order){
       return orderRepo.save(order);
    }
}
