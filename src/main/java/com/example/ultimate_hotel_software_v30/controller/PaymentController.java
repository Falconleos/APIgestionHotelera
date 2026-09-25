package com.example.ultimate_hotel_software_v30.controller;

import com.example.ultimate_hotel_software_v30.dto.request.PaymentDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.PaymentDTOResponse;
import com.example.ultimate_hotel_software_v30.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // Registrar un pago en una cuenta
    @PostMapping
    public ResponseEntity<PaymentDTOResponse> addPaymentToAccount(
            @Valid @RequestBody PaymentDTORequest request,
            Principal principal) {
        // principal.getName() nos da el username del usuario logueado actualmente gracias a Spring Security
        String currentUsername = principal != null ? principal.getName() : "system";
        PaymentDTOResponse response = paymentService.addPaymentToAccount(request, currentUsername);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Obtener todos los pagos del sistema
    @GetMapping
    public ResponseEntity<List<PaymentDTOResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    // Obtener un pago específico por su ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTOResponse> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    // Obtener los pagos asociados a una reserva (Booking ID)
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<PaymentDTOResponse>> getPaymentsByBookingId(@PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.getPaymentsByBookingId(bookingId));
    }

    // Eliminar/Anular un pago por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}