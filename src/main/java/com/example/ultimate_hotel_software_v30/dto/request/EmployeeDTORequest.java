package com.example.ultimate_hotel_software_v30.dto.request;

import com.example.ultimate_hotel_software_v30.enums.Shift;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EmployeeDTORequest {

    @NotNull(message = "User ID is required to link the employee profile")
    private Long userId;

    private String employeeNumber;

    @NotNull(message = "hire date is required")
    private LocalDate hireDate;

    @NotNull(message = "Shift is required")
    private Shift shift;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be a positive number greater than zero")
    private Double salary;

}
