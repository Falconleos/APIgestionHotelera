package com.example.ultimate_hotel_software_v30.service;

import com.example.ultimate_hotel_software_v30.dto.request.PaymentDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.PaymentDTOResponse;

import java.util.List;

public interface PaymentService {

    PaymentDTOResponse addPaymentToAccount(PaymentDTORequest request, String currentUsername);

    List<PaymentDTOResponse> getPaymentsByBookingId(Long bookingId);

    PaymentDTOResponse getPaymentById(Long id);

    List<PaymentDTOResponse> getAllPayments();

    void deletePayment(Long id);
}