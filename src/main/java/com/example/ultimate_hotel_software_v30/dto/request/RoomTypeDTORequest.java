package com.example.ultimate_hotel_software_v30.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "Datos requeridos para crear o actualizar un tipo de habitación")
public class RoomTypeDTORequest {

    @Schema(description = "Nombre único del tipo de habitación", example = "SUITE_DELUXE", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "The name is necessary")
    @Size(min = 1, max = 50, message = "The name must be between 3 and 50 characters")
    private String name;

    @Schema(description = "Capacidad máxima de personas", example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Capacity is necessary")
    @Positive(message = "Capacity must be a positive number greater than zero")
    private Integer capacity;

    @Schema(description = "Descripción detallada de los servicios del tipo de habitación", example = "Habitación con cama King Size, jacuzzi y vista al mar", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Description is necessary")
    @Size(max = 250, message = "Description cannot exceed 250 characters")
    private String description;

    @Schema(description = "Precio por noche de estadía", example = "150.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Price per night is necessary")
    @Positive(message = "Price must be a positive number greater than zero")
    private Double pricePerNight;

}
