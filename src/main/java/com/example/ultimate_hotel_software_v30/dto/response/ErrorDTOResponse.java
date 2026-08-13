package com.example.ultimate_hotel_software_v30.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
@Schema(description = "DTO utilizado para mostrar informacion de los errores")
@Getter
public class ErrorDTOResponse {

    @Schema(description = "Sello de tiempo del error")
    private LocalDateTime timeStamp;
    @Schema(description = "Mensaje de error")
    private String mensaje;
    @Schema(description = "Descripcion del error")
    private String descripcion;

    public ErrorDTOResponse(String mensaje, String descripcion) {
        this.timeStamp = LocalDateTime.now();
        this.mensaje = mensaje;
        this.descripcion = descripcion;
    }

}
