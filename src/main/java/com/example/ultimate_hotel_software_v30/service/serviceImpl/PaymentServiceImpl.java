package com.example.ultimate_hotel_software_v30.service.serviceImpl;

import com.example.ultimate_hotel_software_v30.dto.request.PaymentDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.PaymentDTOResponse;
import com.example.ultimate_hotel_software_v30.mapper.PaymentMapper;
import com.example.ultimate_hotel_software_v30.model.AccountEntity;
import com.example.ultimate_hotel_software_v30.model.PaymentEntity;
import com.example.ultimate_hotel_software_v30.model.UserEntity;
import com.example.ultimate_hotel_software_v30.repository.AccountRepository;
import com.example.ultimate_hotel_software_v30.repository.PaymentRepository;
import com.example.ultimate_hotel_software_v30.repository.UserRepository;
import com.example.ultimate_hotel_software_v30.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentDTOResponse addPaymentToAccount(PaymentDTORequest request, String currentUsername) {
        AccountEntity account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con ID: " + request.getAccountId()));

        UserEntity user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + currentUsername));

        PaymentEntity payment = paymentMapper.toPaymentEntity(request);
        payment.setAccountEntity(account);
        payment.setUserEntity(user);

        paymentRepository.save(payment);
        account.getPayments().add(payment);

        recalculateAccountFinancials(account);

        return paymentMapper.toPaymentDTOResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDTOResponse> getPaymentsByBookingId(Long bookingId) {
        AccountEntity account = accountRepository.findByBookingEntity_Id(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada para la reserva ID: " + bookingId));

        return account.getPayments().stream()
                .map(paymentMapper::toPaymentDTOResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDTOResponse getPaymentById(Long id) {
        PaymentEntity payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con ID: " + id));
        return paymentMapper.toPaymentDTOResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDTOResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(paymentMapper::toPaymentDTOResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePayment(Long id) {
        PaymentEntity payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con ID: " + id));

        AccountEntity account = payment.getAccountEntity();

        if (account != null) {
            // Removemos el pago de la colección de la cuenta
            account.getPayments().remove(payment);
            // Recalculamos los financieros para reflejar que se eliminó el dinero abonado
            recalculateAccountFinancials(account);
        }

        paymentRepository.delete(payment);
    }

    /**
     * Método auxiliar privado para centralizar el recálculo financiero de la cuenta.
     */
    private void recalculateAccountFinancials(AccountEntity account) {
        double base = account.getBaseAmount() != null ? account.getBaseAmount() : 0.0;
        double services = account.getServicesTotal() != null ? account.getServicesTotal() : 0.0;
        int adjustment = account.getAdjustmentPercentage() != null ? account.getAdjustmentPercentage() : 0;

        double subtotal = base + services;
        double finalTotal = subtotal + (subtotal * adjustment / 100.0);

        double totalPaid = account.getPayments().stream()
                .mapToDouble(PaymentEntity::getAmount)
                .sum();

        account.setPaidAmount(totalPaid);
        account.setIsPaid(totalPaid >= finalTotal);

        accountRepository.save(account);
    }
}