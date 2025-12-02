package Orders.controllers;

import Orders.DTOs.OrderCashierDTO;
import Orders.DTOs.OrderCustomerDTO;
import Orders.domain.Order;
import Orders.mappers.OrderMapper;
import Orders.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMapper orderMapper;


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
}
