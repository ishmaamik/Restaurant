package Payment.domain;

import Payment.DTOs.PaymentRequestDTO;
import Payment.DTOs.PaymentResponseDTO;

public interface IPayment {
    public PaymentResponseDTO Pay(PaymentRequestDTO paymentRequestDTO);
}
