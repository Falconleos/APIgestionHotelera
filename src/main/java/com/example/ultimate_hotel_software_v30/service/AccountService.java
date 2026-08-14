package com.example.ultimate_hotel_software_v30.service;

import com.example.ultimate_hotel_software_v30.dto.request.PaymentDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.AccountDTOResponse;
import com.example.ultimate_hotel_software_v30.dto.response.PaymentDTOResponse;

import java.util.List;

public interface AccountService {
    List<AccountDTOResponse> getAllAccounts();
    AccountDTOResponse getAccountById(Long id);
    AccountDTOResponse getAccountByBookingId(Long bookingId);
    void addChargeToAccount(Long bookingId, Double amount);
    void subtractChargeFromAccount(Long bookingId, Double amount);
    AccountDTOResponse updateAdjustmentPercentage(Long bookingId, Integer adjustmentPercentage);
    PaymentDTOResponse addPaymentToAccount(PaymentDTORequest request, String currentUsername);
    List<PaymentDTOResponse> getPaymentsByBookingId(Long bookingId);
}