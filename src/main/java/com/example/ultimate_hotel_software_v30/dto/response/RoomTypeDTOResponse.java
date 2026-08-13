package com.example.ultimate_hotel_software_v30.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "Datos de respuesta con la información detallada del tipo de habitación")
public class RoomTypeDTOResponse {

    @Schema(description = "Identificador único del tipo de habitación", example = "1")
    private Long id;

    @Schema(description = "Nombre comercial del tipo de habitación", example = "Suite Doble Deluxe")
    private String name;

    @Schema(description = "Capacidad máxima de pasajeros permitidos", example = "2")
    private Integer capacity;

    @Schema(description = "Descripción detallada de las comodidades y características", example = "Habitación con cama matrimonial, vista al mar, aire acondicionado y jacuzzi")
    private String description;

    @Schema(description = "Precio base por noche en la moneda configurada", example = "75000.00")
    private Double pricePerNight;

}