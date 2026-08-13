package com.example.ultimate_hotel_software_v30.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa los estados posibles de una reserva en el sistema")
public enum BookingState {

    @Schema(description = "Reserva tomada pero aún no confirmada con seña/pago")
    PENDING,

    @Schema(description = "Reserva confirmada por el hotel")
    CONFIRMED,

    @Schema(description = "El huésped ya hizo el check-in y está ocupando la habitación")
    CHECKED_IN,

    @Schema(description = "La estadía finalizó con éxito y se realizó el check-out")
    CONCLUDED,

    @Schema(description = "El huésped no se presentó en la fecha de check-in")
    NO_SHOW, // Equivalente a AUSENTE

    @Schema(description = "La reserva fue cancelada antes de la fecha")
    CANCELLED,

    @Schema(description = "La estadía se dio por terminada antes de lo previsto originalmente")
    INTERRUPTED

}
