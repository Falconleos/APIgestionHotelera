package com.example.ultimate_hotel_software_v30.dto.response;

import com.example.ultimate_hotel_software_v30.enums.Shift;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de respuesta con la información detallada del empleado")
public class EmployeeDTOResponse {

    @Schema(description = "Identificador único del empleado (coincide con el ID del usuario asociado)", example = "15")
    private Long id;

    @Schema(description = "Número de teléfono de emergencia del empleado", example = "2239876543")
    private String emergencyPhoneNumber;

    @Schema(description = "Número o legajo de identificación interna del empleado", example = "EMP-0042")
    private String employeeNumber;

    @Schema(description = "Fecha de contratación o ingreso a la empresa", example = "2024-03-01")
    private LocalDate hireDate;

    @Schema(description = "Turno laboral asignado", example = "MORNING")
    private Shift shift;

    @Schema(description = "Sueldo o salario actual del empleado", example = "650000.00")
    private Double salary;

    @Schema(description = "Nombre de usuario asociado al empleado", example = "jperez")
    private String username;

    @Schema(description = "Nombre del empleado", example = "Juan")
    private String name;

    @Schema(description = "Apellido del empleado", example = "Pérez")
    private String surname;

    @Schema(description = "Correo electrónico de contacto", example = "juan.perez@hotel.com")
    private String email;

}