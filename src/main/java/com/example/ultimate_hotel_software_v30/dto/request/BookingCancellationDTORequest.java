package com.example.ultimate_hotel_software_v30.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "Datos requeridos para solicitar la cancelación de una reserva activa")
public class BookingCancellationDTORequest {

    @NotNull(message = "The booking ID is required")
    @Schema(description = "Identificador único de la reserva a cancelar", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long bookingId;

    @Size(max = 250, message = "The reason cannot exceed 250 characters")
    @Schema(description = "Motivo expresado por el pasajero para cancelar", example = "Problemas de salud o de viaje")
    private String reason;

}
