package com.example.ultimate_hotel_software_v30.dto.request;

import com.example.ultimate_hotel_software_v30.enums.RoomState;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
public class RoomDTORequest {
    @NotNull(message = "Room number is required")
    @Positive(message = "Room number must be a positive integer")
    @Schema(description = "Número de la habitación", example = "102", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer number;

    @NotNull(message = "Room state is required")
    @Schema(description = "Estado inicial de la habitación", example = "AVAILABLE", requiredMode = Schema.RequiredMode.REQUIRED)
    private RoomState state;

    @NotNull(message = "Room type ID is required")
    @Schema(description = "Identificador del tipo de habitación", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long roomTypeId;

    @Schema(description = "Lista de imágenes asociadas a la habitación")
    private List<MultipartFile> images;
}