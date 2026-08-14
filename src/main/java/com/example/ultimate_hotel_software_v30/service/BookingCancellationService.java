package com.example.ultimate_hotel_software_v30.service;

import com.example.ultimate_hotel_software_v30.dto.response.BookingCancellationDTOResponse;
import com.example.ultimate_hotel_software_v30.model.BookingCancellationEntity;

import java.util.List;

public interface BookingCancellationService {
    BookingCancellationEntity findEntityById(Long id);

    BookingCancellationDTOResponse findById(Long id);

    List<BookingCancellationDTOResponse> getCancellationHistory();

    List<BookingCancellationDTOResponse> findByGuestLastName(String lastName);

    BookingCancellationEntity create(BookingCancellationEntity cancellation);

    void purgeCancellationHistory();
}
