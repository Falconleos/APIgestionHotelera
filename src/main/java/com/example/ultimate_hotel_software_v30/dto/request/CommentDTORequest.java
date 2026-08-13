package com.example.ultimate_hotel_software_v30.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "Datos requeridos para crear un comentario sobre una estadia")
public class CommentDTORequest {

    @NotNull(message = "Check-in ID is required")
    @Schema(description = "Identificador de la estadia (Check-In) asociada", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long checkInId;

    @NotBlank(message = "Content is required")
    @Size(max = 500, message = "Content cannot exceed 500 characters")
    @Schema(description = "Texto del comentario", example = "Excelente servicio y habitaciÃ³n muy limpia.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    @Schema(description = "ValoraciÃ³n de 1 a 5", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer rating;

}
