package com.example.ultimate_hotel_software_v30.model;

import com.example.ultimate_hotel_software_v30.enums.Shift;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "employees")
public class EmployeeEntity{

    @Id
    private Long id;

    @Column(length = 15, unique = true)
    private String emergencyPhoneNumber;

    @Column(unique = true, length = 20)
    private String employeeNumber;

    @Column(nullable = false)
    private LocalDate hireDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Shift shift;

    @Column(nullable = false)
    private Double salary;

    @OneToOne(fetch = FetchType.LAZY) // Relación 1 a 1 con carga perezosa
    @MapsId // Comparte la clave primaria con UserEntity (mantiene la eficiencia)
    @JoinColumn(name = "user_id") // Nombre de la columna FK/PK en la tabla employees
    private UserEntity user;
}