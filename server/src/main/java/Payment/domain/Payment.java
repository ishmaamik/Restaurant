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

    private Order order;

    private User user;

    private String paymentMethod;

    private UUID transactionId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDateTime createdAt;

    private void OnCreate(){
        this.createdAt= LocalDateTime.now();
    }
}
