package com.example.ultimate_hotel_software_v30.service;

import com.example.ultimate_hotel_software_v30.dto.response.CreditNoteDTOResponse;
import com.example.ultimate_hotel_software_v30.model.AccountEntity;

public interface CreditNoteService {
    CreditNoteDTOResponse createCreditNote(AccountEntity account, String cancellationReason);
}