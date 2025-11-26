package Payment.domain;

import Payment.DTOs.PaymentRequestDTO;
import Payment.DTOs.PaymentResponseDTO;

import java.util.UUID;

public interface IPayment {
    public PaymentResponseDTO Pay(UUID orderId, PaymentRequestDTO request);
}
