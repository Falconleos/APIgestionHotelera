package com.example.ultimate_hotel_software_v30.repository;

import com.example.ultimate_hotel_software_v30.enums.BookingState;
import com.example.ultimate_hotel_software_v30.model.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity,Long> {

    List<BookingEntity> findByActive(Boolean active);
    boolean existsByRoomEntity_Id(Long roomId);

    List<BookingEntity> findByBookingState(BookingState state);
}
