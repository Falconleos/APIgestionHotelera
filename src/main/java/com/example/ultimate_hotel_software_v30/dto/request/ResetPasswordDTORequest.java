package com.example.ultimate_hotel_software_v30.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos requeridos para restablecer la contraseña utilizando un token de recuperación válido")
public class ResetPasswordDTORequest {
    @Schema(
            description = "Token único de recuperación enviado previamente por correo electrónico",
            example = "123e4567-e89b-12d3-a456-426614174000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "the token is necessary")
    private String token;

    @Schema(
            description = "La nueva contraseña que se asignará a la cuenta del usuario",
            example = "MyNewSecurePassword2026!",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "new password is necessary")
    private String newPassword;
}
