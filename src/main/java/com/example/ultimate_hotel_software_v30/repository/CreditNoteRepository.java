package com.example.ultimate_hotel_software_v30.repository;

import com.example.ultimate_hotel_software_v30.model.CreditNoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditNoteRepository extends JpaRepository<CreditNoteEntity, Long> {

    // Buscar por ID de cuenta
    List<CreditNoteEntity> findByAccountId(Long accountId);

    List<CreditNoteEntity> findByAccountBookingEntityId(Long bookingId);

    List<CreditNoteEntity> findByAccountBookingEntityUserEntityId(Long userId);
}