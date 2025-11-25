package Payment.DTOs;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentResponseDTO {
    private UUID transId;
    private String message;
    private boolean success;
}
