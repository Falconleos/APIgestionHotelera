package com.example.ultimate_hotel_software_v30.dto.request;

import com.example.ultimate_hotel_software_v30.enums.Role;
import com.example.ultimate_hotel_software_v30.enums.Shift;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreateUnifiedDTO {

    @Schema(
            description = "Nombre de usuario único para iniciar sesión",
            example = "johndoe99",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Username is necessary")
    private String username;

    @Schema(
            description = "Contraseña de acceso a la cuenta",
            example = "SecretPassword123!",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Password is necessary")
    private String password;

    @Schema(
            description = "Primer y segundo nombre del usuario",
            example = "John",
            minLength = 3,
            maxLength = 50,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Name is necessary")
    @Size(min = 3, max = 50, message = "Name must contain between 3 and 50 characters")
    private String name;

    @Schema(
            description = "Apellidos del usuario",
            example = "Doe",
            minLength = 3,
            maxLength = 50,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Surname is necessary")
    @Size(min = 3, max = 50, message = "Surname must contain between 3 and 50 characters")
    private String surname;

    @Schema(
            description = "Documento Nacional de Identidad (DNI), solo números (7 a 10 dígitos)",
            example = "45678912",
            pattern = "\\d{7,10}",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Dni is necessary")
    @Pattern(
            regexp = "\\d{7,10}",
            message = "Dni must contain between 7 and 10 characters"
    )
    private String dni;

    @Schema(
            description = "Género del usuario",
            example = "Masculino"
    )
    @Size(max = 20, message = "Gender cannot exceed 20 characters")
    private String gender;

    @Schema(
            description = "Dirección de correo electrónico válida y única",
            example = "john.doe@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Email is necessary")
    @Email(message = "The email must be valid")
    private String email;

    @Schema(
            description = "Número de teléfono celular",
            example = "2231234567"
    )
    @Size(max = 15, message = "Phone number cannot exceed 15 characters")
    private String phoneNumber;

    @Schema(
            description = "Dirección física del usuario",
            example = "Av. Colón 1234"
    )
    @Size(max = 150, message = "Address cannot exceed 150 characters")
    private String address;

    @Schema(
            description = "Fecha de nacimiento (debe ser una fecha pasada)",
            example = "1995-05-15",
            type = "string",
            format = "date",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Birth day is necessary")
    @Past(message = "BirthDay must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd") // <-- Agregado para parseo desde FormData
    private LocalDate birthDay;

    @Schema(description = "Rol asignado al usuario", example = "RECEPCIONIST", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Role is necessary")
    private Role role;

    @Schema(
            description = "Archivo de imagen para la foto de perfil (Formatos permitidos: JPG, JPEG, PNG. Tamaño máximo recomendado: 2MB)",
            type = "string",
            format = "binary"
    )
    private MultipartFile profilePictureFile;

    @Schema(description = "Estado de activación de la cuenta", example = "true")
    private Boolean enabled;

    // Datos del Empleado
    private String employeeNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd") // <-- Agregado para parseo desde FormData
    private LocalDate hireDate;

    private Shift shift;
    private Double salary;
    private String emergencyPhoneNumber;
}