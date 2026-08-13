package com.example.ultimate_hotel_software_v30.repository;

import com.example.ultimate_hotel_software_v30.model.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByCheckInId(Long checkInId);
    List<CommentEntity> findByUserId(Long userId);
    boolean existsByCheckInIdAndUserId(Long checkInId, Long userId);
}