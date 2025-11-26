package Payment.domain;

import Payment.DTOs.PaymentRequestDTO;
import Payment.DTOs.PaymentResponseDTO;

import java.util.UUID;

public interface IPayment {
    public PaymentResponseDTO Pay(PaymentRequestDTO request);
    //No need for orderId as payment should not care about orders
}
