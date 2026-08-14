package com.example.ultimate_hotel_software_v30.controller;

import com.example.ultimate_hotel_software_v30.dto.request.RoomAttentionDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.RoomAttentionDTOResponse;
import com.example.ultimate_hotel_software_v30.service.RoomAttentionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/private/room-attentions")
@RequiredArgsConstructor
public class RoomAttentionController {

    private final RoomAttentionService roomAttentionService;

    // Tanto Administradores como Recepcionistas pueden ver los consumos de una reserva (Booking)
    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN', 'RECEPCIONIST', 'ROLE_RECEPCIONIST')")
    public ResponseEntity<List<RoomAttentionDTOResponse>> getAttentionsByBookingId(@PathVariable Long bookingId) {
        return ResponseEntity.ok(roomAttentionService.getAttentionsByBookingId(bookingId));
    }

    // Tanto Administradores como Recepcionistas pueden registrar un consumo o servicio
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN', 'RECEPCIONIST', 'ROLE_RECEPCIONIST')")
    public ResponseEntity<RoomAttentionDTOResponse> addAttention(@Valid @RequestBody RoomAttentionDTORequest request) {
        RoomAttentionDTOResponse response = roomAttentionService.addAttention(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Tanto Administradores como Recepcionistas pueden eliminar cargos cargados por error
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN', 'RECEPCIONIST', 'ROLE_RECEPCIONIST')")
    public ResponseEntity<Void> removeAttention(@PathVariable Long id) {
        roomAttentionService.removeAttention(id);
        return ResponseEntity.noContent().build();
    }
}