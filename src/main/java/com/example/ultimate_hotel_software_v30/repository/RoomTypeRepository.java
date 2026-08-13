package com.example.ultimate_hotel_software_v30.repository;
import com.example.ultimate_hotel_software_v30.model.RoomTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomTypeRepository extends JpaRepository<RoomTypeEntity,Long> {
    Optional<RoomTypeEntity> findByName(String name);
    boolean existsByName(String name);
}
