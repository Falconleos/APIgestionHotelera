package com.example.ultimate_hotel_software_v30.dto.response;

import com.example.ultimate_hotel_software_v30.enums.BookingState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de respuesta con la información detallada de la reserva")
public class BookingDTOResponse {

    @Schema(description = "Identificador único de la reserva", example = "1")
    private Long id;

    @Schema(description = "Fecha de ingreso (Check-in)", example = "2026-09-01")
    private LocalDate checkIn;

    @Schema(description = "Fecha de salida (Check-out)", example = "2026-09-07")
    private LocalDate checkOut;

    @Schema(description = "Cantidad de pasajeros (pax)", example = "2")
    private Integer guestCount;

    @Schema(description = "Estado actual de la reserva", example = "CONFIRMED")
    private BookingState state;

    @Schema(description = "Nombre del huésped principal", example = "Carlos")
    private String guestFirstName;

    @Schema(description = "Apellido del huésped principal", example = "Gómez")
    private String guestLastName;

    @Schema(description = "Teléfono de contacto del huésped", example = "2234567890")
    private String guestPhone;

    @Schema(description = "nombre del usuario asociado a la reserva", example = "leonel")
    private String name;

    @Schema(description = "apellido del usuario asociado a la reserva", example = "soto")
    private String surname;

    @Schema(description = "username de usuario asociado a la reserva", example = "carlosgomez")
    private String username;

    @Schema(description = "Código QR o identificador único para validación de la reserva", example = "QR-BOOKING-987654321")
    private String qrBooking;

    @Schema(description = "Observaciones o peticiones especiales de la reserva", example = "Cama matrimonial y habitación en piso alto si es posible")
    private String observation;

    @Schema(description = "username del usuario  que realizó/creó la reserva", example = "jperez")
    private String userBookingUsername;

    @Schema(description = "username del usuario que realizó el Check-in (opcional)", example = "mromero")
    private String userCheckInUsername;

    @Schema(description = "Número de la habitación asignada", example = "204")
    private Integer roomNumber;

    @Schema(description = "Precio total de la reserva", example = "350000.00")
    private Double totalPrice;

    @Schema(description = "Fecha y hora de creación de la reserva", example = "2026-08-13T14:30:00")
    private LocalDateTime createdAt;

}