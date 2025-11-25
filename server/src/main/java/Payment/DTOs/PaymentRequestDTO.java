package Payment.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
    private UUID userId;
    private UUID orderId;
    private String paymentMethod;
    private String phoneNumber; //optional
    private BigDecimal amount;
    private String cardToken;   //optional
}
