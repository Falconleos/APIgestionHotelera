package com.example.ultimate_hotel_software_v30.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de respuesta con la información detallada del ítem o servicio")
public class ItemDTOResponse {

    @Schema(description = "Identificador único del ítem o servicio", example = "1")
    private Long id;

    @Schema(description = "Descripción o nombre del ítem o servicio", example = "Cerveza en lata 473ml")
    private String description;

    @Schema(description = "Stock o cantidad disponible en inventario", example = "50")
    private Integer quantity;

    @Schema(description = "Precio unitario de venta", example = "2500.00")
    private Double unitPrice;

    @Schema(description = "Indica si es un servicio (true) o un producto físico/consumible (false)", example = "false")
    private Boolean isService;

}