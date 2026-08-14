package com.example.ultimate_hotel_software_v30.controller;

import com.example.ultimate_hotel_software_v30.dto.request.ItemDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.ItemDTOResponse;
import com.example.ultimate_hotel_software_v30.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/private/items")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@Tag(name = "Item Controller", description = "Endpoints para la gestión de ítems individuales")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN', 'RECEPCIONIST', 'ROLE_RECEPCIONIST')")
    @Operation(summary = "Listar todos los ítems")
    public ResponseEntity<List<ItemDTOResponse>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN', 'RECEPCIONIST', 'ROLE_RECEPCIONIST')")
    @Operation(summary = "Obtener un ítem por su ID")
    public ResponseEntity<ItemDTOResponse> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN', 'RECEPCIONIST', 'ROLE_RECEPCIONIST')")
    @Operation(summary = "Crear un nuevo ítem")
    public ResponseEntity<ItemDTOResponse> createItem(@Valid @RequestBody ItemDTORequest request) {
        ItemDTOResponse response = itemService.createItem(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN', 'RECEPCIONIST', 'ROLE_RECEPCIONIST')")
    @Operation(summary = "Actualizar un ítem existente")
    public ResponseEntity<ItemDTOResponse> updateItem(@PathVariable Long id, @Valid @RequestBody ItemDTORequest request) {
        ItemDTOResponse response = itemService.updateItem(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN', 'RECEPCIONIST', 'ROLE_RECEPCIONIST')")
    @Operation(summary = "Eliminar un ítem")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}