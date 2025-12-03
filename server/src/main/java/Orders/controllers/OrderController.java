package Orders.controllers;

import Orders.DTOs.OrderCashierDTO;
import Orders.DTOs.OrderCustomerDTO;
import Orders.DTOs.OrderItemDTO;
import Orders.domain.Order;
import Orders.mappers.OrderMapper;
import Orders.services.OrderItemService;
import Orders.services.OrderService;
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
}
