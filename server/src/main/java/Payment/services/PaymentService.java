package Payment.services;

import Payment.DTOs.PaymentRequestDTO;
import Payment.DTOs.PaymentResponseDTO;
import Payment.domain.IPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final Map<String,IPayment> strategies;  //constructor initialized, not field i.e. no @autowired

    public PaymentService(List<IPayment> strategyList){
        this.strategies= strategyList.stream()
                .collect(Collectors.toMap(
                        strategy-> strategy.getClass().getSimpleName().toUpperCase(),
                        strategy-> strategy
                ));
    }

    public PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequestDTO){
        IPayment payment= strategies.get(paymentRequestDTO.getPaymentMethod().toUpperCase()+"STRATEGY");

        if (payment== null){
            throw new UnsupportedOperationException("Payment not supported");
        }

        return payment.Pay(paymentRequestDTO);
    }
}
