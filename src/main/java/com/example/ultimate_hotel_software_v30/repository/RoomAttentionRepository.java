package com.example.ultimate_hotel_software_v30.repository;

import com.example.ultimate_hotel_software_v30.model.RoomAttentionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomAttentionRepository extends JpaRepository<RoomAttentionEntity, Long> {

    // Método corregido para buscar por el ID de la reserva (BookingEntity)
    List<RoomAttentionEntity> findByBookingEntity_Id(Long bookingId);
}