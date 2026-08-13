package com.example.ultimate_hotel_software_v30.repository;
import com.example.ultimate_hotel_software_v30.enums.RoomState;
import com.example.ultimate_hotel_software_v30.model.RoomEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity,Long> {
    Optional<Object> findByNumber(Integer number);
    List<RoomEntity> findByState(RoomState roomState);
}

