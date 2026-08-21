package com.example.ultimate_hotel_software_v30.controller;

import com.example.ultimate_hotel_software_v30.dto.response.CreditNoteDTOResponse;
import com.example.ultimate_hotel_software_v30.service.CreditNoteService;
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
@RequestMapping("/private/credit-notes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@Tag(name = "GestionNotasDeCredito", description = "Endpoints privados para la gestión y consulta de notas de crédito")
public class CreditNoteController {

    private final CreditNoteService creditNoteService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST')")
    @Operation(
            summary = "Listar todas las notas de crédito",
            description = "Devuelve una lista con todas las notas de crédito registradas en el sistema."
    )
    public ResponseEntity<List<CreditNoteDTOResponse>> getAllCreditNotes() {
        return ResponseEntity.ok(creditNoteService.getAllCreditNotes());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST')")
    @Operation(
            summary = "Obtener nota de crédito por ID",
            description = "Devuelve los detalles de una nota de crédito específica mediante su ID."
    )
    public ResponseEntity<CreditNoteDTOResponse> getCreditNoteById(@PathVariable Long id) {
        return ResponseEntity.ok(creditNoteService.getCreditNoteById(id));
    }

    @GetMapping("/account/{accountId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST')")
    @Operation(
            summary = "Buscar notas de crédito por cuenta",
            description = "Devuelve las notas de crédito asociadas a una cuenta específica."
    )
    public ResponseEntity<List<CreditNoteDTOResponse>> getCreditNotesByAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(creditNoteService.getCreditNotesByAccountId(accountId));
    }

    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST')")
    @Operation(
            summary = "Buscar notas de crédito por reserva",
            description = "Devuelve las notas de crédito vinculadas a una reserva específica."
    )
    public ResponseEntity<List<CreditNoteDTOResponse>> getCreditNotesByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(creditNoteService.getCreditNotesByBookingId(bookingId));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST') or (hasRole('GUEST') and #userId == principal.id)")
    @Operation(
            summary = "Buscar notas de crédito por usuario",
            description = "Devuelve las notas de crédito de un usuario. Los huéspedes solo pueden consultar las suyas."
    )
    public ResponseEntity<List<CreditNoteDTOResponse>> getCreditNotesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(creditNoteService.getCreditNotesByUserId(userId));
    }
}