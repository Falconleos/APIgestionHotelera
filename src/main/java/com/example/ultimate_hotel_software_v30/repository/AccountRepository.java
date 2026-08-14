package com.example.ultimate_hotel_software_v30.repository;

import com.example.ultimate_hotel_software_v30.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    // Buscar una cuenta a partir del ID del Booking vinculado
    Optional<AccountEntity> findByBookingEntity_Id(Long bookingId);
}