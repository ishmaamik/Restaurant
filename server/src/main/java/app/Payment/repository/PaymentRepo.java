package app.Payment.repository;

import app.Payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepo extends JpaRepository<Payment, UUID> {
    List<Payment> findPaymentByUser(UUID userId);

    List<Payment> findPaymentByOrder(UUID orderId);

    Optional<Payment> findPaymentByTransactionId(UUID transactionId);
}
