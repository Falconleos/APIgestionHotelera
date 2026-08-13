package com.example.ultimate_hotel_software_v30.repository;

import com.example.ultimate_hotel_software_v30.model.BookingCancellationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingCancellationRepository extends JpaRepository<BookingCancellationEntity,Long> {

    boolean existsByBookingId(Long bookingId);
}
