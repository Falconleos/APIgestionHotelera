package com.example.ultimate_hotel_software_v30.dto.response;

import com.example.ultimate_hotel_software_v30.enums.RoomState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "Datos de respuesta con la información detallada de la habitación")
public class RoomDTOResponse {

    @Schema(description = "Identificador único de la habitación", example = "1")
    private Long id;

    @Schema(description = "Número de la habitación", example = "102")
    private Integer number;

    @Schema(description = "Estado actual de la habitación", example = "AVAILABLE")
    private RoomState state;

    @Schema(description = "Información detallada del tipo de habitación")
    private RoomTypeDTOResponse roomTypeDTOResponse;

    @Schema(description = "Cantidad de imágenes asociadas a la habitación", example = "3")
    private int imagesCount;

}