package com.example.ultimate_hotel_software_v30.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de respuesta con el estado financiero y detalle de la cuenta de la reserva")
public class AccountDTOResponse {

    @Schema(description = "Identificador único de la cuenta", example = "1")
    private Long id;

    @Schema(description = "Identificador de la reserva asociada", example = "10")
    private Long bookingId;

    @Schema(description = "Listado de pagos realizados asociados a la cuenta")
    private List<PaymentDTOResponse> payments;

    @Schema(description = "Monto base fijo correspondiente a la estadía", example = "350000.00")
    private Double baseAmount;

    @Schema(description = "Subtotal acumulado por consumos de servicios o ítems extra", example = "10000.00")
    private Double servicesTotal;

    @Schema(description = "Suma total acumulada de los pagos efectuados", example = "200000.00")
    private Double paidAmount;

    @Schema(description = "Indica si la cuenta se encuentra totalmente pagada (true) o pendiente (false)", example = "false")
    private Boolean isPaid;

    @Schema(description = "Porcentaje de recargo (positivo) o descuento (negativo) aplicado a la cuenta", example = "0")
    private Integer adjustmentPercentage;

    // --- Nuevos campos extraídos de la Reserva y Habitación ---
    @Schema(description = "Datos del huésped principal")
    private UserDetailsDTO user;

    @Schema(description = "Número o nombre de la habitación", example = "102")
    private String roomNumber;

    @Schema(description = "Fecha de check-in", example = "2026-08-20")
    private LocalDate checkInDate;

    @Schema(description = "Fecha de check-out", example = "2026-08-25")
    private LocalDate checkOutDate;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserDetailsDTO {
        private String name;
        private String surname;
        private String dni;
    }
}