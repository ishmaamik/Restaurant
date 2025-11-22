package Orders.domain;

import Orders.enums.ItemStatus;
import Orders.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import Orders.domain.Order;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
public class OrderItem {
    @Id
    @GeneratedValue
    private UUID itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="menu_id", nullable = false)
    private Menu menu;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    private String notes;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus= ItemStatus.PENDING;

    public void calculateTotalPrice(){
        this.totalPrice= unitPrice.multiply(new BigDecimal(quantity));  //convert a copy of quantity to big decimal
    }

    public void addItem(){
        this.quantity+=1;
        calculateTotalPrice();
    }

    public void removeItem(){
        this.quantity-=1;
        calculateTotalPrice();
    }

    public void confirm(){
        if(!this.itemStatus.equals(ItemStatus.PENDING)){
            throw new IllegalStateException("Items must be pending to be confirmed");
        }
        this.itemStatus= ItemStatus.CONFIRMED;
    }

    public void markPreparing(){
        if(!this.itemStatus.equals(OrderStatus.CONFIRMED)){
            throw new IllegalStateException("Cannot prepare not confirmed items");
        }
        this.itemStatus= ItemStatus.PREPARING;
    }

    public void markReady(){
        if(!this.itemStatus.equals(OrderStatus.PREPARING)){
            throw new IllegalStateException("Cannot mark ready not preparing items");
        }
        this.itemStatus= ItemStatus.READY;
    }

    public void markServed(){
        if(!this.itemStatus.equals(OrderStatus.READY)){
            throw new IllegalStateException("Cannot serve not yet ready items");
        }
        this.itemStatus= ItemStatus.SERVED;
    }

}
