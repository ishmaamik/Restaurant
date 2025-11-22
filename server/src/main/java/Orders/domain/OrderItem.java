package Orders.domain;

import Orders.enums.ItemStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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

}
