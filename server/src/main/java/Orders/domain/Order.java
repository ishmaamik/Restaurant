package Orders.domain;
import Orders.enums.OrderStatus;
import Orders.enums.OrderType;
import Payment.enums.PaymentStatus;
import Users.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private UUID orderId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    private String paymentMethod;

    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private OrderType orderType;

    @Column(nullable = false)
    private Integer tableNo;

    @Column(nullable = false)
    private BigDecimal subAmount;

    @Column(nullable = false)
    private BigDecimal taxAmount;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private BigDecimal discountAmount= BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal serviceChargeAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User orderedBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime preparingAt;
    private LocalDateTime readyAt;
    private LocalDateTime servedAt;
    private LocalDateTime paidAt;
    private LocalDateTime cancelledAt;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<OrderItem> items= new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addItem(OrderItem item){
        item.setOrder(this);    //lombok generated this method on Order order of OrderItem
        this.items.add(item);
        this.recalculateAmounts();
    }

    public void removeItem(OrderItem item){
        item.setOrder(null);
        this.items.remove(item); //remove item from order
        this.recalculateAmounts();
    }

    public void recalculateAmounts(){
        this.subAmount= items.stream().map(OrderItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        this.taxAmount= subAmount.multiply(BigDecimal.valueOf(0.10));
        this.serviceChargeAmount= subAmount.multiply(BigDecimal.valueOf(0.05));
        this.totalAmount= subAmount.add(taxAmount).add(serviceChargeAmount).subtract(discountAmount);
    }

    public void confirm(){
        if(!this.orderStatus.equals(OrderStatus.CREATED)){
            throw new IllegalStateException("Not yet created orders cannot be confirmed");
        }
        this.orderStatus= OrderStatus.CONFIRMED;
        this.confirmedAt= LocalDateTime.now();
    }

    public void markPreparing(){
        if(!this.orderStatus.equals(OrderStatus.CONFIRMED)){
            throw new IllegalStateException("Cannot prepare not confirmed orders");
        }
        this.orderStatus= OrderStatus.PREPARING;
        this.preparingAt= LocalDateTime.now();
    }

    public void markReady(){
        if(!this.orderStatus.equals(OrderStatus.PREPARING)){
            throw new IllegalStateException("Cannot mark ready not preparing orders");
        }
        this.orderStatus= OrderStatus.READY;
        this.readyAt=LocalDateTime.now();
    }

    public void markServed(){
        if(!this.orderStatus.equals(OrderStatus.READY)){
            throw new IllegalStateException("Cannot serve not yet ready orders");
        }
        this.orderStatus= OrderStatus.SERVED;
        this.servedAt= LocalDateTime.now();
    }

    public void markPaid(){
        if(!this.orderStatus.equals(OrderStatus.SERVED)){
            throw new IllegalStateException("Cannot mark not yet served orders as Paid");
        }
        this.orderStatus= OrderStatus.PAID;
        this.paidAt= LocalDateTime.now();
    }

    public void cancel(){
        if(!this.orderStatus.equals(OrderStatus.CREATED)){
            throw new IllegalStateException("Cannot mark not created orders as Cancelled");
        }

        if(this.orderStatus.equals(OrderStatus.CANCELLED)){
            throw new IllegalStateException("Cannot mark already cancelled orders as Cancelled");
        }

        if(this.orderStatus.equals(OrderStatus.CONFIRMED)){
            throw new IllegalStateException("Cannot CANCEL CONFIRMED Orders");
        }

        this.orderStatus= OrderStatus.CANCELLED;
        this.cancelledAt= LocalDateTime.now();
    }
}
