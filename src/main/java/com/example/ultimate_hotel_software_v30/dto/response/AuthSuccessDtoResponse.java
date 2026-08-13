package com.example.ultimate_hotel_software_v30.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseCookie;

@Schema(description = "Respuesta final tras una autenticación o registro exitoso")
public record AuthSuccessDtoResponse(

        @Schema(description = "Contenedor que posee el token de acceso JWT")
        AuthTokenResponse authTokenResponse,

        @Schema(description = "Cookie HttpOnly que contiene el Refresh Token (manejada a nivel de cabeceras)", hidden = true)
        ResponseCookie responseCookie
) {
}
