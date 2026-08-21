package com.example.ultimate_hotel_software_v30.repository;

import com.example.ultimate_hotel_software_v30.model.CreditNoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditNoteRepository extends JpaRepository<CreditNoteEntity, Long> {

    // Buscar por ID de cuenta (este ya estaba bien)
    List<CreditNoteEntity> findByAccountId(Long accountId);

    // Como en AccountEntity el campo se llama 'bookingEntity', la ruta correcta es:
    List<CreditNoteEntity> findByAccountBookingEntityId(Long bookingId);

    // Para buscar por usuario, asumiendo que en BookingEntity el usuario se llama 'user' o 'userEntity' y su ID es 'id'.
    // * Si en tu BookingEntity el usuario se llama 'user', usa esta línea:
    List<CreditNoteEntity> findByAccountBookingEntityUserEntityId(Long userId);
}