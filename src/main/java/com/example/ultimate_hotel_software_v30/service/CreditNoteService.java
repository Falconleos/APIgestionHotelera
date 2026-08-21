package com.example.ultimate_hotel_software_v30.service;

import com.example.ultimate_hotel_software_v30.dto.response.CreditNoteDTOResponse;
import com.example.ultimate_hotel_software_v30.model.AccountEntity;

import java.util.List;

public interface CreditNoteService {
    CreditNoteDTOResponse createCreditNote(AccountEntity account, String cancellationReason);

    List<CreditNoteDTOResponse> getAllCreditNotes();

    CreditNoteDTOResponse getCreditNoteById(Long id);

    List<CreditNoteDTOResponse> getCreditNotesByAccountId(Long accountId);

    List<CreditNoteDTOResponse> getCreditNotesByBookingId(Long bookingId);

    List<CreditNoteDTOResponse> getCreditNotesByUserId(Long userId);
}