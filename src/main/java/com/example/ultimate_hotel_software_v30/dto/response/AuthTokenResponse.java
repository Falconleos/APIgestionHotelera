package com.example.ultimate_hotel_software_v30.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Contenedor que transporta el token de acceso JWT")
public record AuthTokenResponse(
        @Schema(
                description = "Token de acceso en formato JWT (Bearer Token) necesario para autenticar las peticiones protegidas",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huZG9lOTkiLCJpYXQiOjE3MTY4..."
        )
        String token) {
}
