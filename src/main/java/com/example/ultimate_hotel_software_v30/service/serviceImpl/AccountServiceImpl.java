package com.example.ultimate_hotel_software_v30.service.serviceImpl;

import com.example.ultimate_hotel_software_v30.dto.request.PaymentDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.AccountDTOResponse;
import com.example.ultimate_hotel_software_v30.dto.response.PaymentDTOResponse;
import com.example.ultimate_hotel_software_v30.mapper.AccountMapper;
import com.example.ultimate_hotel_software_v30.mapper.PaymentMapper;
import com.example.ultimate_hotel_software_v30.model.AccountEntity;
import com.example.ultimate_hotel_software_v30.model.PaymentEntity;
import com.example.ultimate_hotel_software_v30.model.UserEntity;
import com.example.ultimate_hotel_software_v30.repository.AccountRepository;
import com.example.ultimate_hotel_software_v30.repository.BookingRepository;
import com.example.ultimate_hotel_software_v30.repository.UserRepository;
import com.example.ultimate_hotel_software_v30.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AccountDTOResponse> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(accountMapper::toAccountDTOResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDTOResponse getAccountById(Long id) {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con ID: " + id));
        return accountMapper.toAccountDTOResponse(account);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDTOResponse getAccountByBookingId(Long bookingId) {
        AccountEntity account = accountRepository.findByBookingEntity_Id(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada para la reserva ID: " + bookingId));
        return accountMapper.toAccountDTOResponse(account);
    }

    @Override
    @Transactional
    public void addChargeToAccount(Long bookingId, Double amount) {
        AccountEntity account = accountRepository.findByBookingEntity_Id(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada para la reserva ID: " + bookingId));

        double currentServices = account.getServicesTotal() != null ? account.getServicesTotal() : 0.0;
        double addedAmount = amount != null ? amount : 0.0;
        account.setServicesTotal(currentServices + addedAmount);

        recalculateAndSave(account);
    }

    @Override
    @Transactional
    public void subtractChargeFromAccount(Long bookingId, Double amount) {
        AccountEntity account = accountRepository.findByBookingEntity_Id(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada para la reserva ID: " + bookingId));

        double currentServices = account.getServicesTotal() != null ? account.getServicesTotal() : 0.0;
        double subAmount = amount != null ? amount : 0.0;
        account.setServicesTotal(Math.max(0.0, currentServices - subAmount));

        recalculateAndSave(account);
    }

    @Override
    @Transactional
    public AccountDTOResponse updateAdjustmentPercentage(Long bookingId, Integer adjustmentPercentage) {
        AccountEntity account = accountRepository.findByBookingEntity_Id(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada para la reserva ID: " + bookingId));

        account.setAdjustmentPercentage(adjustmentPercentage != null ? adjustmentPercentage : 0);

        recalculateAndSave(account);
        return accountMapper.toAccountDTOResponse(account);
    }

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

        // Agregamos el pago a la lista de la cuenta
        account.getPayments().add(payment);

        recalculateAndSave(account);

        // Retornamos el DTO del pago recién guardado
        PaymentEntity savedPayment = account.getPayments().get(account.getPayments().size() - 1);
        return paymentMapper.toPaymentDTOResponse(savedPayment);
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

    /**
     * Método auxiliar privado para centralizar la lógica de recálculo financiero en el Service.
     */
    private void recalculateAndSave(AccountEntity account) {
        double base = account.getBaseAmount() != null ? account.getBaseAmount() : 0.0;
        double services = account.getServicesTotal() != null ? account.getServicesTotal() : 0.0;
        int adjustment = account.getAdjustmentPercentage() != null ? account.getAdjustmentPercentage() : 0;

        // Subtotal antes de ajuste
        double subtotal = base + services;
        // Aplicar porcentaje de recargo o descuento
        double finalTotal = subtotal + (subtotal * adjustment / 100.0);

        // Sumar todos los pagos registrados
        double totalPaid = account.getPayments().stream()
                .mapToDouble(PaymentEntity::getAmount)
                .sum();

        account.setPaidAmount(totalPaid);
        // Se marca como pagado si lo abonado cubre o supera el total final calculado
        account.setIsPaid(totalPaid >= finalTotal);

        accountRepository.save(account);
    }
}