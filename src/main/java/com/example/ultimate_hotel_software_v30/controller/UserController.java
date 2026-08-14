package com.example.ultimate_hotel_software_v30.controller;

import com.example.ultimate_hotel_software_v30.dto.request.ChangePasswordDTORequest;
import com.example.ultimate_hotel_software_v30.dto.request.UserDTORequest;
import com.example.ultimate_hotel_software_v30.dto.request.UserDTORequestCreation;
import com.example.ultimate_hotel_software_v30.dto.response.UserDTOResponse;
import com.example.ultimate_hotel_software_v30.service.UserService;
import com.example.ultimate_hotel_software_v30.service.serviceImpl.AuthService;
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
@RequestMapping("/private/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@Tag(name = "GestionUsuarios", description = "Endpoints privados para la gestión de funcionalidades de usuarios")

public class UserController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/change-password")
    @Operation(
            summary = "Cambiar contraseña interna",
            description = "Permite a un usuario autenticado modificar su contraseña actual validando primero su clave vigente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contraseña actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "La contraseña actual provista es incorrecta"),
            @ApiResponse(responseCode = "401", description = "No autorizado / No se encontró usuario en el contexto de seguridad")
    })
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordDTORequest request) {
        authService.changePassword(request);
        return ResponseEntity.ok().build();
    }

    //-----------------------------recepcionista o administrador crean usuario---------------------------//

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST')")
    @Operation(
            summary = "Crear nuevo usuario (Personal)",
            description = "Permite a un administrador o recepcionista registrar un nuevo usuario en el sistema con una contraseña temporal."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "El email o DNI ya existen en el sistema"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<UserDTOResponse> createUser(@Valid @RequestBody UserDTORequestCreation userDtoRequestCreation) {
        return new ResponseEntity<>(userService.createUserWithRole(userDtoRequestCreation), HttpStatus.CREATED);
    }

    /*------recepcionista o administrador obtienen por id y usuario solo su id---------------------------*/

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST') or (hasRole('GUEST') and #id == principal.id)")
    @Operation(
            summary = "Obtener usuario por ID",
            description = "Devuelve los detalles de un usuario. Los huéspedes solo pueden consultarse a sí mismos."
    )
    public ResponseEntity<UserDTOResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    /*------recepcionista o administrador obtienen por dni---------------------------*/
    @GetMapping("/dni/{dni}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST')")
    @Operation(
            summary = "Buscar usuario por DNI",
            description = "Permite buscar un usuario directamente usando su número de DNI."
    )
    public ResponseEntity<UserDTOResponse> getByDni(@PathVariable String dni) {
        return ResponseEntity.ok(userService.userByDni(dni));
    }

    /*------recepcionista o administrador obtienen por dni---------------------------*/
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST')")
    @Operation(
            summary = "Listar todos los usuarios",
            description = "Devuelve una lista con todos los usuarios registrados en el hotel."
    )
    public ResponseEntity<List<UserDTOResponse>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    /*------recepcionista o administrador actualizan por id y usuario solo su id---------------------------*/
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST') or (hasRole('GUEST') and #id == principal.id)")
    @Operation(
            summary = "Actualizar datos del usuario",
            description = "Permite modificar la información personal. Los huéspedes solo pueden modificar su propio perfil."
    )
    public ResponseEntity<UserDTOResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTORequest userDtoRequest) {
        return ResponseEntity.ok(userService.updateUser(id, userDtoRequest));
    }

    /*------administrador elimina---------------------------*/
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Eliminar un usuario",
            description = "Elimina físicamente a un usuario del sistema mediante su ID. Acción exclusiva del Administrador."
    )
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
