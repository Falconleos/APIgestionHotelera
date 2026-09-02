package com.example.ultimate_hotel_software_v30.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de la notificación generada al registrar una nueva atención o consumo")
public class RoomAttentionNotificationDTO {

    @Schema(description = "ID de la atención", example = "1")
    private Long id;

    @Schema(description = "ID de la reserva asociada", example = "10")
    private Long bookingId;

    @Schema(description = "Descripción del ítem solicitado", example = "Cerveza en lata 473ml (Minibar)")
    private String itemDescription;

    @Schema(description = "Cantidad", example = "2")
    private Integer quantity;

    @Schema(description = "Fecha y hora de la solicitud", example = "2026-09-02T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Empleado que registró la atención", example = "Juan Pérez")
    private String employeeUsername;

    @Schema(description = "Estado de entrega inicial", example = "false")
    private Boolean delivered;
}