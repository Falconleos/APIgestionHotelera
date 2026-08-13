package com.example.ultimate_hotel_software_v30.dto.request;

import com.example.ultimate_hotel_software_v30.enums.BookingState;
import com.example.ultimate_hotel_software_v30.model.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "Datos requeridos para crear una nueva reserva")
public class BookingDTORequest {

    @NotNull(message = "Check-in date is required")
    @FutureOrPresent(message = "The check-in date must be today or a future date")
    @Schema(description = "Fecha de ingreso (Check-In)", example = "2026-08-15", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate checkIn;

    @NotNull(message = "Check-out date is required")
    @FutureOrPresent(message = "The check-out date must be in the future")
    @Schema(description = "Fecha de egreso (Check-Out)", example = "2026-08-20", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate checkOut;

    @NotNull(message = "Guest count is required")
    @Min(value = 1, message = "The reservation must have at least 1 guest")
    @Schema(description = "Cantidad total de pasajeros (Pax)", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer guestCount;

    @NotBlank(message = "Guest first name is required")
    @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
    @Schema(description = "Nombre del pasajero titular", example = "Juan")
    private String guestFirstName;

    @NotBlank(message = "Guest last name is required")
    @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
    @Schema(description = "Apellido del pasajero titular", example = "Pérez")
    private String guestLastName;

    @NotBlank(message = "Guest phone number is required")
    @Schema(description = "Teléfono de contacto del titular", example = "+5492231234567")
    private String guestPhone;

    @Schema(description = "Identificador del usuario registrado (opcional, null si es reserva telefónica/rápida)", example = "15")
    private Long userId;

    @Size(max = 250, message = "The observation cannot exceed 250 characters")
    @Schema(description = "Aclaraciones o pedidos especiales del pasajero", example = "Solicita cuna para bebé o vista externa")
    private String observation;

    @NotNull(message = "Room ID is required")
    @Schema(description = "Identificador de la habitación física a reservar", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long roomId;

}
