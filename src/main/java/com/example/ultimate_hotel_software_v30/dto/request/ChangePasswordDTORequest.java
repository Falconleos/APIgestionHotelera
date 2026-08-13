package com.example.ultimate_hotel_software_v30.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos requeridos para que un usuario autenticado cambie su contraseña actual")
public class ChangePasswordDTORequest {

    @Schema(
            description = "La contraseña actual del usuario para validar su identidad",
            example = "OldSecurePass123!",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "current password is necesary")
    private String currentPassword;

    @Schema(
            description = "La nueva contraseña que el usuario desea establecer (debe ser diferente a la actual)",
            example = "NewSuperSecret2026*",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "new password is necesary")
    private String newPassword;

}
