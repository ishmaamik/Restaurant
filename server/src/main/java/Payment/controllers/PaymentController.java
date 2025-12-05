package Payment.controllers;

import Payment.DTOs.PaymentRequestDTO;
import Payment.DTOs.PaymentResponseDTO;
import Payment.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private PaymentService paymentService;

    @PostMapping("/{orderId}/pay")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentResponseDTO> pay(@PathVariable UUID orderId, @RequestBody PaymentRequestDTO paymentRequestDTO){
        return ResponseEntity.ok(paymentService.processPayment(orderId, paymentRequestDTO));
    }
}
