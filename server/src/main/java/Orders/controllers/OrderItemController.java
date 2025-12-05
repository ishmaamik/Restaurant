package Orders.controllers;

import Orders.DTOs.OrderItemDTO;
import Orders.mappers.OrderItemMapper;
import Orders.services.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/order-item")
public class OrderItemController {

    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderItemService orderItemService;

    @PostMapping("/{orderItemId}/increment")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderItemDTO> increment(@PathVariable UUID orderItemId){
        return ResponseEntity.ok(orderItemMapper.toOrderItemDTO(orderItemService.increment(orderItemId)));
    }

    @PostMapping("/{orderItemId}/decrement")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderItemDTO> decrement(@PathVariable UUID orderItemId){
        return ResponseEntity.ok(orderItemMapper.toOrderItemDTO(orderItemService.decrement(orderItemId)));
    }

    @PostMapping("/{orderItemId}/prepare")
    @PreAuthorize("hasRole('COOK')")
    public ResponseEntity<OrderItemDTO> prepare(@PathVariable UUID orderItemId){
        return ResponseEntity.ok(orderItemMapper.toOrderItemDTO(orderItemService.prepareItem(orderItemId)));
    }

    @PostMapping("/{orderItemId}/ready")
    @PreAuthorize("hasRole('COOK')")
    public ResponseEntity<OrderItemDTO> ready(@PathVariable UUID orderItemId){
        return ResponseEntity.ok(orderItemMapper.toOrderItemDTO(orderItemService.readyItem(orderItemId)));
    }

    @PostMapping("/{orderItemId}/serve")
    @PreAuthorize("hasRole('WAITER')")
    public ResponseEntity<OrderItemDTO> serve(@PathVariable UUID orderItemId){
        return ResponseEntity.ok(orderItemMapper.toOrderItemDTO(orderItemService.serveItem(orderItemId)));
    }

    @PostMapping("/{orderItemId}/cancel")
    @PreAuthorize("hasAnyRole('WAITER','CUSTOMER','ADMIN')")
    public ResponseEntity<OrderItemDTO> cancel(@PathVariable UUID orderItemId){
        return ResponseEntity.ok(orderItemMapper.toOrderItemDTO(orderItemService.cancelItem(orderItemId)));
    }
}
