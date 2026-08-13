package com.example.ultimate_hotel_software_v30.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "Credenciales necesarias para que un usuario registrado inicie sesión en el sistema")

public class UserLoginDTORequest {

    @Schema(
            description = "Nombre de usuario registrado",
            example = "johndoe99",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "username is necessary")
    private String username;

    @Schema(
            description = "Contraseña asociada a la cuenta del usuario",
            example = "SecretPassword123!",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "password is necessary")
    private String password;


}
