package com.example.ultimate_hotel_software_v30.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta emitida al renovar con éxito el token de acceso utilizando un Refresh Token")

public record RefreshTokenDTOResponse(
        @Schema(
                description = "Nuevo token de acceso JWT generado",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huZG9lOTkiLCJpYXQiOjE3MTY4..."
        )
        String accessToken,

        @Schema(
                description = "Tipo de token emitido para la cabecera de autorización",
                example = "Bearer"
        )
        String tokenType
) {

    public RefreshTokenDTOResponse(String accessToken) {
        this(accessToken, "Bearer");
    }

}
