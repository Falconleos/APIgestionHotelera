package com.example.ultimate_hotel_software_v30.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de respuesta con la información detallada de la cancelación de una reserva")
public class BookingCancellationDTOResponse {

    @Schema(description = "Identificador único del registro de cancelación", example = "1")
    private Long id;

    @Schema(description = "Identificador de la reserva cancelada", example = "10")
    private Long bookingId;

    @Schema(description = "nombre y apellido del empleado que procesó la cancelación", example = "Juan Perez")
    private String nombreApellido;

    @Schema(description = "Nombre de usuario del empleado que procesó la cancelación", example = "jperez")
    private String employeeUsername;

    @Schema(description = "Fecha y hora exacta en la que se realizó la cancelación", example = "2026-08-13T14:30:00")
    private LocalDateTime cancellationDate;

    @Schema(description = "Motivo o razón de la cancelación", example = "Cambio de planes de viaje del pasajero")
    private String reason;

}