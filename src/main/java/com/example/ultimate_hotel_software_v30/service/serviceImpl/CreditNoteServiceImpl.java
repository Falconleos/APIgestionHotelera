package com.example.ultimate_hotel_software_v30.service.serviceImpl;

import com.example.ultimate_hotel_software_v30.dto.response.CreditNoteDTOResponse;
import com.example.ultimate_hotel_software_v30.model.AccountEntity;
import com.example.ultimate_hotel_software_v30.model.CreditNoteEntity;
import com.example.ultimate_hotel_software_v30.repository.CreditNoteRepository;
import com.example.ultimate_hotel_software_v30.service.CreditNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreditNoteServiceImpl implements CreditNoteService {

    private final CreditNoteRepository creditNoteRepository;

    @Override
    @Transactional
    public CreditNoteDTOResponse createCreditNote(AccountEntity account, String cancellationReason) {
        CreditNoteEntity creditNote = CreditNoteEntity.builder()
                .account(account)
                .amount(account.getPaidAmount()) // El monto devuelto es el pagado hasta el momento
                .reason(cancellationReason)      // Heredado de la reserva
                .build();

        CreditNoteEntity savedNote = creditNoteRepository.save(creditNote);
        return mapToResponse(savedNote);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CreditNoteDTOResponse> getAllCreditNotes() {
        return creditNoteRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CreditNoteDTOResponse getCreditNoteById(Long id) {
        CreditNoteEntity creditNote = creditNoteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nota de crédito no encontrada con ID: " + id));
        return mapToResponse(creditNote);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CreditNoteDTOResponse> getCreditNotesByAccountId(Long accountId) {
        return creditNoteRepository.findByAccountId(accountId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CreditNoteDTOResponse> getCreditNotesByBookingId(Long bookingId) {
        return creditNoteRepository.findByAccountBookingEntityId(bookingId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CreditNoteDTOResponse> getCreditNotesByUserId(Long userId) {
        return creditNoteRepository.findByAccountBookingEntityId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Método auxiliar privado para mapear la entidad a DTO de respuesta y evitar repetir código
     */
    private CreditNoteDTOResponse mapToResponse(CreditNoteEntity entity) {
        return CreditNoteDTOResponse.builder()
                .id(entity.getId())
                .accountId(entity.getAccount() != null ? entity.getAccount().getId() : null)
                .amount(entity.getAmount())
                .reason(entity.getReason())
                .issuedAt(entity.getIssuedAt())
                .build();
    }
}