package com.example.ultimate_hotel_software_v30.controller;

import com.example.ultimate_hotel_software_v30.dto.response.BookingCancellationDTOResponse;
import com.example.ultimate_hotel_software_v30.service.BookingCancellationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/private/booking-cancellation")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@Tag(name = "BookingCancellation", description = "Endpoints para la auditoría y control de cancelaciones de reservas")
public class BookingCancellationController {

    private final BookingCancellationService cancellationService;

    /*--------- 1. Obtener una Cancelación por ID (ADMIN) ---------------*/
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN')")
    @Operation(summary = "Obtener detalles de una cancelación por ID", description = "Recupera la información del registro de cancelación física y la reserva asociada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cancelación encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "ID de cancelación no encontrado")
    })
    public ResponseEntity<BookingCancellationDTOResponse> getById(@PathVariable Long id) {
        BookingCancellationDTOResponse cancellation = cancellationService.findById(id);
        return ResponseEntity.ok(cancellation);
    }

    /*--------- 2.1. Listar Historial Completo (ADMIN) ---------------*/
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN')")
    @Operation(summary = "Listar todo el historial de cancelaciones", description = "Devuelve el historial completo de cancelaciones registradas en el sistema.")
    public ResponseEntity<List<BookingCancellationDTOResponse>> getCancellationHistory() {
        List<BookingCancellationDTOResponse> history = cancellationService.getCancellationHistory();
        return ResponseEntity.ok(history);
    }

    /*--------- 2.2. Filtrar Cancelaciones por Apellido (ADMIN) ---------------*/
    @GetMapping("/guest/{guestLastName}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN')")
    @Operation(summary = "Buscar cancelaciones por apellido del huésped", description = "Recupera las cancelaciones cuyo huésped principal coincida con el apellido provisto en la ruta.")
    public ResponseEntity<List<BookingCancellationDTOResponse>> getCancellationByLastName(@PathVariable String guestLastName) {
        List<BookingCancellationDTOResponse> history = cancellationService.findByGuestLastName(guestLastName);
        return ResponseEntity.ok(history);
    }

    /*--------- 3. Depurar Historial de Cancelaciones mayor a un mes (ADMIN) ---------------*/
    @DeleteMapping("/purge")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN')")
    @Operation(summary = "Depurar historial de cancelaciones", description = "Elimina de forma permanente de la base de datos las cancelaciones de más de 30 días de antigüedad.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Historial depurado correctamente")
    })
    public ResponseEntity<Void> purgeCancellations() {
        cancellationService.purgeCancellationHistory();
        return ResponseEntity.noContent().build();
    }
}