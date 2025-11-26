package Orders.services;

import Orders.domain.OrderItem;
import Orders.repository.OrderItemRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepo orderItemRepo;

    public OrderItem getItem(UUID itemId){
        OrderItem item= orderItemRepo.findById(itemId)
                .orElseThrow( ()-> new EntityNotFoundException("Not Found"));
        return item;
    }

    public OrderItem increment(UUID itemId){
        OrderItem item= getItem(itemId);
        item.addItem();
        return orderItemRepo.save(item);
    }

    public OrderItem decrement(UUID itemId){
        OrderItem item= getItem(itemId);
        item.removeItem();
        return orderItemRepo.save(item);
    }

    public OrderItem readyItem(UUID itemId){
        OrderItem item= getItem(itemId);
        item.markReady();
        return orderItemRepo.save(item);
    }

    public OrderItem serveItem(UUID itemId){
        OrderItem item= getItem(itemId);
        item.markServed();
        return orderItemRepo.save(item);
    }

    public OrderItem cancelItem(UUID itemId){
        OrderItem item= getItem(itemId);
        item.markCancelled();
        return orderItemRepo.save(item);
    }
}
