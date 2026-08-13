package com.example.ultimate_hotel_software_v30.repository;

import com.example.ultimate_hotel_software_v30.model.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity,Long> {

    // Buscar reservas filtrando por si están activas o no
    List<BookingEntity> findByActive(Boolean active);

    // Verificar si ya existe alguna reserva activa asociada a una habitación física específica
    boolean existsByRoom_Id(Long roomId);
}
