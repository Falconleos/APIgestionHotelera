package com.example.ultimate_hotel_software_v30.service.serviceImpl;

import com.example.ultimate_hotel_software_v30.dto.response.CreditNoteDTOResponse;
import com.example.ultimate_hotel_software_v30.model.AccountEntity;
import com.example.ultimate_hotel_software_v30.model.CreditNoteEntity;
import com.example.ultimate_hotel_software_v30.repository.CreditNoteRepository;
import com.example.ultimate_hotel_software_v30.service.CreditNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        return CreditNoteDTOResponse.builder()
                .id(savedNote.getId())
                .accountId(savedNote.getAccount().getId())
                .amount(savedNote.getAmount())
                .reason(savedNote.getReason())
                .issuedAt(savedNote.getIssuedAt())
                .build();
    }
}