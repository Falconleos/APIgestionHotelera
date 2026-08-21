package com.example.ultimate_hotel_software_v30.controller;

import com.example.ultimate_hotel_software_v30.dto.request.PaymentDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.AccountDTOResponse;
import com.example.ultimate_hotel_software_v30.dto.response.PaymentDTOResponse;
import com.example.ultimate_hotel_software_v30.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/private/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN', 'RECEPCIONIST', 'ROLE_RECEPCIONIST')")
    public ResponseEntity<List<AccountDTOResponse>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN', 'RECEPCIONIST', 'ROLE_RECEPCIONIST')")
    public ResponseEntity<Object> getAccountById(@PathVariable Long id) {
        try {
            AccountDTOResponse account = accountService.getAccountById(id);
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN', 'RECEPCIONIST', 'ROLE_RECEPCIONIST')")
    public ResponseEntity<Object> getAccountByBookingId(@PathVariable Long bookingId) {
        try {
            AccountDTOResponse account = accountService.getAccountByBookingId(bookingId);
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/booking/{bookingId}/adjustment")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<Object> updateAdjustmentPercentage(
            @PathVariable Long bookingId,
            @RequestBody Map<String, Integer> requestBody) {
        try {
            Integer percentage = requestBody.get("adjustmentPercentage");
            AccountDTOResponse updatedAccount = accountService.updateAdjustmentPercentage(bookingId, percentage);
            return ResponseEntity.ok(updatedAccount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/payments")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN', 'RECEPCIONIST', 'ROLE_RECEPCIONIST')")
    public ResponseEntity<Object> addPayment(
            @Valid @RequestBody PaymentDTORequest request,
            Authentication authentication) {
        try {
            String currentUsername = authentication != null ? authentication.getName() : null;
            PaymentDTOResponse paymentResponse = accountService.addPaymentToAccount(request, currentUsername);
            return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/bookings/{bookingId}/payments")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN', 'RECEPCIONIST', 'ROLE_RECEPCIONIST')")
    public ResponseEntity<Object> getPaymentsByBookingId(@PathVariable Long bookingId) {
        try {
            List<PaymentDTOResponse> payments = accountService.getPaymentsByBookingId(bookingId);
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }




}