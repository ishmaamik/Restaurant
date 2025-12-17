package app.Orders.controllers;

import app.Orders.DTOs.CreateOrderDTO;
import app.Orders.DTOs.OrderCashierDTO;
import app.Orders.DTOs.OrderCustomerDTO;
import app.Orders.DTOs.OrderItemDTO;
import app.Orders.domain.Order;
import app.Orders.enums.OrderStatus;
import app.Orders.mappers.OrderMapper;
import app.Orders.services.OrderService;
import app.Orders.services.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemService orderItemService;

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")  // or permitAll() if customers should create orders
    public ResponseEntity<CreateOrderDTO> createOrder(@RequestBody CreateOrderDTO dto) {
        // Implementation using orderService.createOrder()
        return ResponseEntity.ok(orderMapper.toCreateOrderDTO(orderService.createOrder(dto.getUserId(), dto.getTableNo())));
    }

    @GetMapping("/{userId}/all")
    public ResponseEntity<List<OrderCustomerDTO>> getCustomerOrders(@PathVariable UUID userId){
        List<Order> orders= orderService.getOrderByCustomer(userId);
        return ResponseEntity.ok(orderMapper.toCustomerDTOList(orders));
    }

    @GetMapping("/{orderId}/receipt")
    public ResponseEntity<OrderCashierDTO> getCashierReceipt(@PathVariable UUID orderId){
        Order order= orderService.getOrder(orderId);
        return ResponseEntity.ok(orderMapper.toCashierOrderDTO(order));
    }

    @GetMapping("/{orderId}/user-receipt")
    public ResponseEntity<OrderCustomerDTO> getCustomerReceipt(@PathVariable UUID orderId){
        Order order= orderService.getOrder(orderId);
        return ResponseEntity.ok(orderMapper.toCustomerOrderDTO(order));
    }

    //DTOs always in request body and easier to extend rather than path variables
    @PostMapping("/{orderId}/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderCustomerDTO> addItem(@RequestBody OrderItemDTO orderItemDTO, @PathVariable UUID orderId){
        return ResponseEntity.ok(orderMapper.toCustomerOrderDTO(orderService.addItem(orderId, orderItemDTO.getMenuId())));
    }

    @PostMapping("/{orderId}/remove")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderCustomerDTO> removeItem(@PathVariable UUID orderId, @RequestBody OrderItemDTO orderItemDTO){
        return ResponseEntity.ok(orderMapper.toCustomerOrderDTO(orderService.removeItem(orderId, orderItemDTO.getMenuId())));
    }

    @PostMapping("/{orderId}/confirm")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<OrderCustomerDTO> confirm(@PathVariable UUID orderId){
       return ResponseEntity.ok(orderMapper.toCustomerOrderDTO(orderService.confirmOrder(orderId)));
    }

    @PostMapping("/{orderId}/change-status")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER', 'COOK', 'CASHIER')")
    public ResponseEntity<OrderCustomerDTO> changeStatus(@PathVariable UUID orderId,@RequestBody OrderStatus orderStatus){
        return ResponseEntity.ok(orderMapper.toCustomerOrderDTO(orderService.changeStatus(orderId, orderStatus)));
    }

}
