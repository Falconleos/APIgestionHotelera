package com.example.ultimate_hotel_software_v30.dto.response;

import com.example.ultimate_hotel_software_v30.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de respuesta con la información del rol del sistema")
public class RoleDTOResponse {

    @Schema(description = "Identificador único del rol", example = "1")
    private Long id;

    @Schema(description = "Nombre del rol en el sistema", example = "ROLE_ADMIN")
    private Role name;

    @Schema(description = "Descripción detallada de los permisos y alcances del rol", example = "Administrador con acceso total a la gestión del hotel, usuarios y reportes")
    private String description;

}