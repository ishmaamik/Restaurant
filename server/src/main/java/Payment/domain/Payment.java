package Payment.domain;

import Orders.domain.Order;
import Payment.enums.PaymentStatus;
import Users.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class Payment {
    @Id
    @GeneratedValue
    private UUID paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String paymentMethod;

    private UUID transactionId;

    private BigDecimal amount;

    private PaymentStatus paymentStatus;

    private LocalDateTime createdAt;


    @PrePersist
    protected void OnCreate(){
        this.createdAt= LocalDateTime.now();
    }
}
