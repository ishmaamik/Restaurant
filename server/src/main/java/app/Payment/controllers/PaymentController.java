package app.Payment.controllers;

import app.Payment.services.PaymentService;
import app.Payment.DTOs.PaymentRequestDTO;
import app.Payment.DTOs.PaymentResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{orderId}/pay")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentResponseDTO> pay(@PathVariable UUID orderId, @RequestBody PaymentRequestDTO paymentRequestDTO){
        return ResponseEntity.ok(paymentService.processPayment(orderId, paymentRequestDTO));
    }
}
