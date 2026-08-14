package com.example.ultimate_hotel_software_v30.config;

import com.example.ultimate_hotel_software_v30.enums.Role;
import com.example.ultimate_hotel_software_v30.enums.Shift;
import com.example.ultimate_hotel_software_v30.model.EmployeeEntity;
import com.example.ultimate_hotel_software_v30.model.RoleEntity;
import com.example.ultimate_hotel_software_v30.model.RoomTypeEntity;
import com.example.ultimate_hotel_software_v30.model.UserEntity;
import com.example.ultimate_hotel_software_v30.repository.EmployeeRepository;
import com.example.ultimate_hotel_software_v30.repository.RoleRepository;
import com.example.ultimate_hotel_software_v30.repository.RoomTypeRepository;
import com.example.ultimate_hotel_software_v30.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Recorremos los valores del Enum Role y los creamos si no existen
        for (Role role : Role.values()) {
            if (roleRepository.findByRole(role).isEmpty()) {
                roleRepository.save(
                        RoleEntity.builder()
                                .role(role)
                                .build());
            }
        }

        // 2. Crear Administrador por defecto si no existe ninguno
        String adminUsername = "admin";
        if (userRepository.findByUsername(adminUsername).isEmpty()) {

            RoleEntity adminRole = roleRepository.findByRole(Role.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: ADMIN role not found during initialization."));

            UserEntity defaultAdmin = UserEntity.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode("admin123"))
                    .name("Admin")
                    .surname("System")
                    .dni("00000000")
                    .email("admin@hotel.com")
                    .phoneNumber("123456789")
                    .birthDay(LocalDate.of(1990, 1, 1))
                    .createAt(LocalDate.now())
                    .roles(Set.of(adminRole))
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(true)
                    .build();

            // Capturamos la entidad persistida devuelta por el save()
            UserEntity savedAdmin = userRepository.save(defaultAdmin);
            System.out.println(">> Default admin user successfully created (ID: " + savedAdmin.getId() + ") <<");

            EmployeeEntity employeeEntity = EmployeeEntity.builder()
                    .userEntity(savedAdmin) // Ajustado al nombre real en tu EmployeeEntity
                    .shift(Shift.MORNING)
                    .salary(0.0)
                    .hireDate(LocalDate.now()) // Requerido por la entidad (nullable = false)
                    .build();

            savedAdmin.setEmployeeEntity(employeeEntity); // Ajustado al nombre real en UserEntity

            employeeRepository.save(employeeEntity);
            System.out.println(">> Default admin employee successfully created (ID: " + savedAdmin.getId() + ") <<");
        }

        // 3. Crear RoomType por defecto ("Basic") si no existe
        String defaultRoomTypeName = "Basic";
        if (!roomTypeRepository.existsByName(defaultRoomTypeName)) {
            RoomTypeEntity defaultType = RoomTypeEntity.builder()
                    .name(defaultRoomTypeName)
                    .capacity(1)
                    .description("Categoría temporal por defecto")
                    .pricePerNight(0.0)
                    .build();

            roomTypeRepository.save(defaultType);
            System.out.println(">> Default RoomType 'Basic' successfully created <<");
        }
    }
}