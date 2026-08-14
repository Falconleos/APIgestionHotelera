package com.example.ultimate_hotel_software_v30.dto.request;

import com.example.ultimate_hotel_software_v30.enums.Role; // <-- Importar el enum
import com.example.ultimate_hotel_software_v30.enums.Shift;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreateUnifiedDTO {
    // Datos del Usuario
    private String username;
    private String password;
    private String name;
    private String surname;
    private String dni;
    private String email;
    private String phoneNumber;
    private LocalDate birthDay;
    private Role role; // <-- Nuevo campo para indicar el rol (RECEPCIONIST, MAINTENANCE, etc.)

    // Datos del Empleado
    private String employeeNumber;
    private LocalDate hireDate;
    private Shift shift;
    private Double salary;
    private String emergencyPhoneNumber;
}