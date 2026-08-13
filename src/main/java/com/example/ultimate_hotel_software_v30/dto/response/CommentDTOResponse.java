package com.example.ultimate_hotel_software_v30.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de respuesta con la información del comentario y calificación")
public class CommentDTOResponse {

    @Schema(description = "Identificador único del comentario", example = "1")
    private Long id;

    @Schema(description = "Texto o contenido del comentario", example = "Excelente atención y muy buena la habitación, totalmente recomendada.")
    private String content;

    @Schema(description = "Calificación otorgada (de 1 a 5)", example = "5")
    private Integer rating;

    @Schema(description = "Fecha y hora exacta en la que se realizó el comentario", example = "2026-08-13T14:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Nombre del usuario", example = "Juan")
    private String name;

    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String surname;

    @Schema(description = "Nombre de usuario de quien realizó el comentario", example = "johndoe99")
    private String username;

}