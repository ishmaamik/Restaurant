package app.Payment.domain;

import app.Payment.DTOs.PaymentRequestDTO;
import app.Payment.DTOs.PaymentResponseDTO;

public interface IPayment {
    public PaymentResponseDTO Pay(PaymentRequestDTO request);
    //No need for orderId as payment should not care about orders
}
