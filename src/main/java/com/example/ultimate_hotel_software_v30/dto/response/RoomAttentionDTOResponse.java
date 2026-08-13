package com.example.ultimate_hotel_software_v30.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de respuesta con el detalle del consumo o atención cargada a la habitación")
public class RoomAttentionDTOResponse {

    @Schema(description = "Identificador único del registro de atención", example = "1")
    private Long id;

    @Schema(description = "Identificador de la reserva asociada", example = "10")
    private Long bookingId;

    @Schema(description = "Identificador del ítem o servicio consumido", example = "5")
    private Long itemId;

    @Schema(description = "Descripción o nombre del ítem o servicio", example = "Cerveza en lata 473ml (Minibar)")
    private ItemDTOResponse itemDTOResponse;

    @Schema(description = "Indica si el registro corresponde a un servicio (true) o un producto (false)", example = "false")
    private Boolean isService;

    @Schema(description = "Cantidad consumida", example = "2")
    private Integer quantity;

    @Schema(description = "Precio unitario del ítem al momento del consumo", example = "2500.00")
    private Double unitPrice;

    @Schema(description = "Subtotal calculado (cantidad por precio unitario)", example = "5000.00")
    private Double subtotal;

    @Schema(description = "Fecha y hora exacta en la que se registró el consumo", example = "2026-08-13T14:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Nombre del empleado que cargó o cobró el consumo", example = "Juan Pérez")
    private String employeeUsername;

}