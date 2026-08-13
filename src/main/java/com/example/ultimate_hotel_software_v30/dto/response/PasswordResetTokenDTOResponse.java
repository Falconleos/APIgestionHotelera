package com.example.ultimate_hotel_software_v30.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de respuesta con la información del token de restablecimiento de contraseña")
public class PasswordResetTokenDTOResponse {

    @Schema(description = "Identificador único del token de restablecimiento", example = "1")
    private Long id;

    @Schema(description = "Cadena alfanumérica única del token para recuperación", example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
    private String token;

    @Schema(description = "Identificador del usuario asociado al token", example = "15")
    private Long userId;

    @Schema(description = "Nombre de usuario propietario del token", example = "johndoe99")
    private String username;

    @Schema(description = "Fecha y hora exacta de expiración del token", example = "2026-08-13T15:30:00")
    private LocalDateTime expiryDate;

    @Schema(description = "Indica si el token ya se encuentra expirado", example = "false")
    private boolean expired;

}