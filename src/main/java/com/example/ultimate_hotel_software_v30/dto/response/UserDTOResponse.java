package com.example.ultimate_hotel_software_v30.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "Datos de respuesta del usuario registrado en el sistema")
public class UserDTOResponse {

    @Schema(description = "Identificador único del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre de usuario para el inicio de sesión", example = "johndoe99")
    private String username;

    @Schema(description = "Roles asignados al usuario en el sistema")
    private Set<String> roles; // O puedes mapearlo a un DTO de roles si lo prefieres

    @Schema(description = "Nombre del usuario", example = "John")
    private String name;

    @Schema(description = "Apellido del usuario", example = "Doe")
    private String surname;

    @Schema(description = "Documento Nacional de Identidad", example = "45678912")
    private String dni;

    @Schema(description = "Género del usuario", example = "Masculino")
    private String gender;

    @Schema(description = "Correo electrónico de contacto", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Número de teléfono celular", example = "2231234567")
    private String phoneNumber;

    @Schema(description = "Dirección física", example = "Av. Colón 1234")
    private String address;

    @Schema(description = "Fecha de nacimiento", example = "1995-05-15")
    private LocalDate birthDay;

    @Schema(description = "Fecha de alta o creación de la cuenta", example = "2026-06-01")
    private LocalDate createAt;

    @Schema(description = "Indica si la cuenta no ha expirado", example = "true")
    private boolean accountNonExpired;

    @Schema(description = "Indica si la cuenta no está bloqueada", example = "true")
    private boolean accountNonLocked;

    @Schema(description = "Indica si las credenciales no han expirado", example = "true")
    private boolean credentialsNonExpired;

    @Schema(description = "Indica si el usuario está habilitado", example = "true")
    private boolean enabled;

    @Schema(description = "Indica si el usuario tiene una foto de perfil cargada", example = "true")
    private boolean hasProfilePicture;
}
