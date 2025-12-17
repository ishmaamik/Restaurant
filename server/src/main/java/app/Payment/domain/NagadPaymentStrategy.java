package app.Payment.domain;

import app.Payment.DTOs.PaymentRequestDTO;
import app.Payment.DTOs.PaymentResponseDTO;
import app.Payment.enums.PaymentStatus;

import java.util.UUID;

public class NagadPaymentStrategy implements IPayment{
    @Override
    public PaymentResponseDTO Pay(PaymentRequestDTO paymentRequestDTO) {

        UUID transId= UUID.randomUUID();

        if(paymentRequestDTO.getPhoneNumber()==null){
            return new PaymentResponseDTO(transId, "Nagad Requires Phone Number", false, PaymentStatus.FAILED);
        }

        return new PaymentResponseDTO(transId, "Transaction successful", true, PaymentStatus.SUCCESS);
    }
}
