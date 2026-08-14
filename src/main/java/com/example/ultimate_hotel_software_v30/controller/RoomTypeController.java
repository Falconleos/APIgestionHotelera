package com.example.ultimate_hotel_software_v30.controller;

import com.example.ultimate_hotel_software_v30.dto.request.RoomTypeDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.RoomTypeDTOResponse;
import com.example.ultimate_hotel_software_v30.mapper.RoomTypeMapper;
import com.example.ultimate_hotel_software_v30.model.RoomTypeEntity;
import com.example.ultimate_hotel_software_v30.service.RoomTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/private/room-type")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@Tag(name = "GestionTipoHabitaciones", description = "Endpoints privados para administrar las categorías y tipos de habitaciones del hotel")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;
    private final RoomTypeMapper roomTypeMapper;

    /*--------- Crear un nuevo tipo de habitación (ADMIN) ---------------*/
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear un nuevo tipo de habitación", description = "Permite registrar una nueva categoría de habitación con su precio y capacidad.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tipo de habitación creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "El nombre de la categoría ya existe o datos inválidos"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<RoomTypeDTOResponse> createRoomType(@Valid @RequestBody RoomTypeDTORequest request) {
        RoomTypeEntity entityInput = roomTypeMapper.toRoomTypeEntity(request);
        RoomTypeEntity entitySaved = roomTypeService.createRoomType(entityInput);
        return new ResponseEntity<>(roomTypeMapper.toRoomTypeDTOResponse(entitySaved), HttpStatus.CREATED);
    }

    /*--------- Obtener tipo de habitación por ID (ADMIN y RECEPCIONIST) ---------------*/
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST')")
    @Operation(summary = "Obtener tipo de habitación por ID")
    public ResponseEntity<RoomTypeDTOResponse> getById(@PathVariable Long id) {
        RoomTypeEntity entity = roomTypeService.getRoomTypeById(id);
        return ResponseEntity.ok(roomTypeMapper.toRoomTypeDTOResponse(entity));
    }

    /*--------- Obtener todos los tipos de habitaciones---------------*/
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST','GUEST')")
    @Operation(summary = "Listar todos los tipos de habitaciones")
    public ResponseEntity<List<RoomTypeDTOResponse>> getAll() {
        List<RoomTypeDTOResponse> list = roomTypeService.getAllRoomTypes().stream()
                .map(roomTypeMapper::toRoomTypeDTOResponse)
                .toList();
        return ResponseEntity.ok(list);
    }

    /*--------- Actualizar tipo de habitación (ADMIN) ---------------*/
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar tipo de habitación existente")
    public ResponseEntity<RoomTypeDTOResponse> updateRoomType(
            @PathVariable Long id,
            @Valid @RequestBody RoomTypeDTORequest request
    ) {
        RoomTypeEntity details = roomTypeMapper.toRoomTypeEntity(request);
        RoomTypeEntity updatedEntity = roomTypeService.updateRoomType(id, details);
        return ResponseEntity.ok(roomTypeMapper.toRoomTypeDTOResponse(updatedEntity));
    }

    /*--------- Eliminar tipo de habitación (ADMIN) ---------------*/
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar un tipo de habitación", description = "Elimina físicamente el tipo de habitación de la base de datos si no tiene habitaciones activas asociadas.")
    public ResponseEntity<Void> deleteRoomType(@PathVariable Long id) {
        roomTypeService.deleteRoomType(id);
        return ResponseEntity.noContent().build();
    }

}
