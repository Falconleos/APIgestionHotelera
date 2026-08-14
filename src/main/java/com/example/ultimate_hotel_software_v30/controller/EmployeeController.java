package com.example.ultimate_hotel_software_v30.controller;

import com.example.ultimate_hotel_software_v30.dto.request.EmployeeCreateUnifiedDTO;
import com.example.ultimate_hotel_software_v30.dto.request.EmployeeDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.EmployeeDTOResponse;
import com.example.ultimate_hotel_software_v30.enums.Shift;
import com.example.ultimate_hotel_software_v30.service.EmployeeService;
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
@RequestMapping("/private/employee")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@Tag(name = "GestionEmpleados", description = "Endpoints privados para la administración del personal del hotel")
public class EmployeeController {

    private final EmployeeService employeeService;

    /*---------admin crea empleado---------------*/
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Crear perfil de empleado",
            description = "Asocia datos laborales (turno y salario) a un usuario ya existente en el sistema. Acción exclusiva de Administradores."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Perfil de empleado creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "El usuario provisto no existe o ya tiene un perfil de empleado asociado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<EmployeeDTOResponse> createEmployee(@Valid @RequestBody EmployeeCreateUnifiedDTO request) {
        return new ResponseEntity<>(employeeService.createEmployee(request), HttpStatus.CREATED);
    }

    /*---------recepcionista o admin accede a empleado por id---------------*/
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST') or #id == principal.id")
    @Operation(
            summary = "Obtener empleado por ID",
            description = "Devuelve los datos laborales e información de usuario de un empleado por su ID. El propio empleado puede consultarse a sí mismo."
    )
    public ResponseEntity<EmployeeDTOResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getById(id));
    }

    /*---------admin obtiene a todos los empleados---------------*/
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Listar todos los empleados",
            description = "Devuelve un listado completo con todos los empleados registrados y sus respectivos turnos y salarios."
    )
    public ResponseEntity<List<EmployeeDTOResponse>> getAll() {
        return ResponseEntity.ok(employeeService.getAll());
    }

    /*---------admin actualiza a empleado---------------*/
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Actualizar perfil de empleado",
            description = "Permite modificar los datos laborales (turno y sueldo) del empleado. Solo accesible por el Administrador."
    )
    public ResponseEntity<EmployeeDTOResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTORequest request
    ) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }

    /*---------admin actualiza el turno a empleado---------------*/
    @PatchMapping("/{id}/shift")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Cambiar turno de un empleado",
            description = "Actualiza de forma aislada el turno asignado a un empleado."
    )
    public ResponseEntity<EmployeeDTOResponse> cambiarTurno(
            @PathVariable Long id,
            @RequestParam Shift nuevoShift
    ) {
        return ResponseEntity.ok(employeeService.cambiarTurno(id, nuevoShift));
    }

    /*---------admin elimina a empleado---------------*/
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Eliminar perfil de empleado",
            description = "Elimina la información laboral del empleado de la base de datos. Nota: Esto no elimina su cuenta de usuario base."
    )
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

}
