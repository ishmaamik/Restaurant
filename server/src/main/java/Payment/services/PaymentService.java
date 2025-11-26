package Payment.services;

import Orders.domain.Order;
import Orders.enums.OrderStatus;
import Orders.services.OrderService;
import Payment.DTOs.PaymentRequestDTO;
import Payment.DTOs.PaymentResponseDTO;
import Payment.domain.IPayment;
import Payment.domain.Payment;
import Payment.enums.PaymentStatus;
import Payment.repository.PaymentRepo;
import Users.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service    //I could put required args constructor only if I did not have to customize my strategies mapping
@Transactional
@Getter//ensures that if it fails midway nothing saves, either full save or none, ensures consistency
@Setter
public class PaymentService {

    private final Map<String,IPayment> strategies;  //constructor initialized, not field i.e. no @autowired

    @Autowired
    private OrderService orderService;    //@Autowired would make it field injection which is test hostile

    @Autowired
    private PaymentRepo paymentRepo;

    public PaymentService(List<IPayment> strategyList, OrderService orderService, PaymentRepo paymentRepo){
        this.strategies= strategyList.stream()
                .collect(Collectors.toMap(
                        strategy-> strategy.getClass().getSimpleName().toUpperCase(),
                        strategy-> strategy
                ));
    }

    public Order updatePaymentStatus(Order order, PaymentStatus paymentStatus){
        order.setPaymentStatus(paymentStatus);
        return orderService.save(order);
    }

    public PaymentResponseDTO processPayment(UUID orderId, PaymentRequestDTO request){

        IPayment payment= strategies.get(request.getPaymentMethod().toUpperCase()+"STRATEGY");
        Order order= orderService.getOrder(orderId);
        if (payment== null){
            updatePaymentStatus(order, PaymentStatus.FAILED);
            throw new UnsupportedOperationException("Payment not supported");
        }

        PaymentResponseDTO response= payment.Pay(request);


        User user= order.getOrderedBy();
        Payment paymentEntity= Payment.builder()
                .order(order)
                .user(user)
                .transactionId(response.getTransId())
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(response.getPaymentStatus())
                .amount(request.getAmount())
                .build();

        updatePaymentStatus(order, response.getPaymentStatus());
        paymentRepo.save(paymentEntity);
        if(response.getPaymentStatus()== PaymentStatus.SUCCESS){
            orderService.changeStatus(orderId, OrderStatus.PAID);
        }
        else{
            orderService.changeStatus(orderId, OrderStatus.PAYMENT_FAILED);
        }
        return response;
    }
}
