package Payment.domain;

import Payment.DTOs.PaymentRequestDTO;
import Payment.DTOs.PaymentResponseDTO;

import java.util.UUID;

public class NagadPaymentStrategy implements IPayment{
    @Override
    public PaymentResponseDTO Pay(PaymentRequestDTO paymentRequestDTO) {

        UUID transId= UUID.randomUUID();

        if(paymentRequestDTO.getPhoneNumber()==null){
            return new PaymentResponseDTO(transId, "Nagad Requires Phone Number", false);
        }

        return new PaymentResponseDTO(transId, "Transaction successful", true);
    }
}
