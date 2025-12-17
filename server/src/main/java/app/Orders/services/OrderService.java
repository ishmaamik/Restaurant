package app.Orders.services;

import app.Menu.domain.Menu;
import app.Menu.services.MenuService;
import app.Orders.domain.Order;
import app.Orders.enums.ItemStatus;
import app.Orders.enums.OrderStatus;
import app.Orders.repository.OrderRepo;
import app.Orders.domain.OrderItem;
import app.Payment.enums.PaymentStatus;
import app.Users.domain.User;
import app.Users.services.UserService;
import app.Websocket.services.OrderNotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepo orderRepo;
    private final MenuService menuService;
    private final UserService userService;
    private final OrderNotificationService notificationService;

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
        Order saved = orderRepo.save(order);
        notificationService.notifyStatusChange(saved, "SYSTEM");
        return saved;
    }

    public Order getOrder(UUID orderId){
       return orderRepo.findById(orderId)
                .orElseThrow(()->new EntityNotFoundException("Order not found"));
    }

    public List<Order> getOrderByCustomer(UUID userId){
        return orderRepo.findByOrderedBy_UserId(userId)
                .orElseThrow(()-> new EntityNotFoundException("No orders from this customer"));
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
                .itemStatus(ItemStatus.IN_QUEUE)
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
        Order saved= save(order);
        notificationService.notifyStatusChange(saved, "SYSTEM");
        return saved;
    }

    public Order prepareOrder(UUID orderId, User user){
        Order order= getOrder(orderId);
        order.markPreparing();
        Order saved = save(order);
        notificationService.notifyStatusChange(saved, user.getUsername());
        return saved;
    }

    public Order readyOrder(UUID orderId, User user){
        Order order= getOrder(orderId);
        order.markReady();
        Order saved = save(order);
        notificationService.notifyStatusChange(saved, user.getUsername());
        return saved;
    }

    public Order serveOrder(UUID orderId, User user){
        Order order= getOrder(orderId);
        order.markServed();
        Order saved = save(order);
        notificationService.notifyStatusChange(saved, user.getUsername());
        return saved;
    }

    public Order paidOrder(UUID orderId, User user){
        Order order= getOrder(orderId);
        order.markPaid();
        Order saved = save(order);
        notificationService.notifyStatusChange(saved, user.getUsername());
        return saved;
    }

    public Order cancelOrder(UUID orderId, User user){
        Order order= getOrder(orderId);
        order.cancel();
        Order saved = save(order);
        notificationService.notifyStatusChange(saved, user.getUsername());
        return saved;
    }

    public Order save(Order order){
       return orderRepo.save(order);
    }
}
