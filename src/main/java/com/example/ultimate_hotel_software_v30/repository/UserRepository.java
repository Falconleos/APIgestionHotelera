package com.example.ultimate_hotel_software_v30.repository;

import com.example.ultimate_hotel_software_v30.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity>findByUsername(String username);
    Optional<UserEntity>findByEmail(String email);
    Optional<UserEntity>findByDni(String dni);
    boolean existsByDni(String dni);
    boolean existsByEmail(String email);

}
