package com.example.ultimate_hotel_software_v30.controller;

import com.example.ultimate_hotel_software_v30.dto.request.BookingCancellationDTORequest;
import com.example.ultimate_hotel_software_v30.dto.request.BookingDTORequest;
import com.example.ultimate_hotel_software_v30.dto.request.PaymentDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.BookingCancellationDTOResponse;
import com.example.ultimate_hotel_software_v30.dto.response.BookingDTOResponse;
import com.example.ultimate_hotel_software_v30.dto.response.PaymentDTOResponse;
import com.example.ultimate_hotel_software_v30.dto.response.RoomDTOResponse;
import com.example.ultimate_hotel_software_v30.enums.BookingState;
import com.example.ultimate_hotel_software_v30.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/private/booking")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS}, allowCredentials = "true")
@Tag(name = "Booking", description = "Endpoints para la administración y control de reservas")
public class BookingController {

    private final BookingService bookingService;

    /*--------- 1. Crear una Reserva (ADMIN o RECEPCIONIST) ---------------*/
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST')")
    @Operation(summary = "Crear una nueva reserva", description = "Registra una reserva asociando empleado, habitación, fechas y calcula el precio total.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva creada con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o reglas de negocio violadas")
    })
    public ResponseEntity<BookingDTOResponse> createBooking(@Valid @RequestBody BookingDTORequest request) {
        BookingDTOResponse newBooking = bookingService.createBooking(request);
        return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
    }

    /*--------- 2. Confirmar Reserva (ADMIN o RECEPCIONIST) ---------------*/
    @PatchMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST')")
    @Operation(summary = "Confirmar reserva", description = "Cambia el estado de una reserva de PENDING a CONFIRMED.")
    public ResponseEntity<BookingDTOResponse> confirmBooking(@PathVariable Long id) {
        BookingDTOResponse confirmed = bookingService.confirmBooking(id);
        return ResponseEntity.ok(confirmed);
    }

    /*--------- 3. Cancelar Reserva (ADMIN o RECEPCIONIST) ---------------*/
    @PostMapping("/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST')")
    @Operation(summary = "Cancelar una reserva activa", description = "Cancela la reserva, la desactiva y genera el registro físico de la cancelación.")
    public ResponseEntity<BookingCancellationDTOResponse> cancelBooking(@Valid @RequestBody BookingCancellationDTORequest request) {
        BookingCancellationDTOResponse cancellation = bookingService.cancelBooking(request);
        return new ResponseEntity<>(cancellation, HttpStatus.CREATED);
    }

    /*--------- 4. Obtener Reserva por ID (ADMIN o RECEPCIONIST) ---------------*/
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST')")
    @Operation(summary = "Obtener detalles de una reserva por ID")
    public ResponseEntity<BookingDTOResponse> getById(@PathVariable Long id) {
        BookingDTOResponse booking = bookingService.findById(id);
        return ResponseEntity.ok(booking);
    }

    /*--------- 5. Listar todas las reservas (ADMIN o RECEPCIONIST) ---------------*/
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST')")
    @Operation(summary = "Listar todas las reservas", description = "Devuelve el historial completo de reservas (activas e inactivas).")
    public ResponseEntity<List<BookingDTOResponse>> getAllBookings() {
        List<BookingDTOResponse> bookings = bookingService.getBookings(null);
        return ResponseEntity.ok(bookings);
    }

    /*--------- 5.2. Listar reservas filtradas por estado de actividad (ADMIN o RECEPCIONIST) ---------------*/
    @GetMapping("/active/{active}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST')")
    @Operation(summary = "Listar reservas por estado activo/inactivo", description = "Filtra las reservas según estén activas (true) o inactivas (false) usando variables de ruta.")
    public ResponseEntity<List<BookingDTOResponse>> getBookingsByActive(@PathVariable Boolean active) {
        List<BookingDTOResponse> bookings = bookingService.getBookings(active);
        return ResponseEntity.ok(bookings);
    }

    /*--------- 6. Buscar Habitaciones Disponibles en Fechas (ADMIN o RECEPCIONIST o GUEST) ---------------*/
    @GetMapping("/available-rooms/{checkIn}/{checkOut}/{guestCount}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST','GUEST')")
    @Operation(summary = "Listar habitaciones disponibles según fechas y huéspedes")
    public ResponseEntity<List<RoomDTOResponse>> getAvailableRooms(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut,
            @PathVariable Integer guestCount
    ) {
        List<RoomDTOResponse> available = bookingService.getAvailableRooms(checkIn, checkOut, guestCount);
        return ResponseEntity.ok(available);
    }

    /*--------- 7. Check-Ins del día de hoy (ADMIN o RECEPCIONIST) ---------------*/
    @GetMapping("/today-checkins")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST')")
    @Operation(summary = "Listar ingresos programados para el día de hoy")
    public ResponseEntity<List<BookingDTOResponse>> getTodayCheckIns() {
        List<BookingDTOResponse> todayCheckIns = bookingService.getCheckInsOfToday();
        return ResponseEntity.ok(todayCheckIns);
    }

    /*--------- 8. Ver reservas pendientes a X días de Check-In (ADMIN o RECEPCIONIST) ---------------*/
    @GetMapping("/pending-to-confirm/{days}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST')")
    @Operation(summary = "Listar reservas pendientes a X días de ingresar", description = "Ideal para controles preventivos o campañas de re-confirmación telefónica.")
    public ResponseEntity<List<BookingDTOResponse>> getBookingsToConfirm(@PathVariable Integer days) {
        List<BookingDTOResponse> toConfirm = bookingService.getBookingsToConfirmInDays(days);
        return ResponseEntity.ok(toConfirm);
    }

    /*--------- 9. Procesar Ausencias / No-Show (ADMIN o RECEPCIONIST) ---------------*/
    @PostMapping("/process-no-shows")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST')")
    @Operation(summary = "Procesar reservas ausentes (No-Show)", description = "Busca reservas pendientes o confirmadas cuya fecha de check-in ya pasó y las inactiva automáticamente.")
    public ResponseEntity<Void> processNoShows() {
        bookingService.processNoShowBookings();
        return ResponseEntity.ok().build();
    }

    /*--------- 10. Realizar Check-In validando por DNI (ADMIN o RECEPCIONIST) ---------------*/
    @PatchMapping("/{bookingId}/check-in")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST')")
    @Operation(summary = "Realizar Check-In", description = "Cambia el estado a CHECKED_IN validando que el usuario exista en el sistema a través de su DNI.")
    public ResponseEntity<BookingDTOResponse> checkInBooking(
            @PathVariable Long bookingId,
            @RequestParam(required = false) String dni) {
        BookingDTOResponse response = bookingService.checkInBooking(bookingId, dni);
        return ResponseEntity.ok(response);
    }

    /*--------- 11. Asignar/Vincular un usuario existente a una reserva (ADMIN o RECEPCIONIST) ---------------*/
    @PatchMapping("/{bookingId}/assign-user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST')")
    @Operation(summary = "Vincular usuario a reserva", description = "Asocia de forma independiente un usuario existente por su ID a una reserva.")
    public ResponseEntity<BookingDTOResponse> assignUserToBooking(
            @PathVariable Long bookingId,
            @PathVariable Long userId) {
        BookingDTOResponse response = bookingService.assignUserToBooking(bookingId, userId);
        return ResponseEntity.ok(response);
    }
    /*--------- 12. Obtener los pagos/señas de una reserva (ADMIN o RECEPCIONIST) ---------------*/
    @GetMapping("/{bookingId}/payments")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST')")
    @Operation(summary = "Listar pagos de una reserva", description = "Devuelve todas las señas o pagos asociados a una reserva específica.")
    public ResponseEntity<List<PaymentDTOResponse>> getPaymentsByBookingId(@PathVariable Long bookingId) {
        List<PaymentDTOResponse> payments = bookingService.getPaymentsByBookingId(bookingId);
        return ResponseEntity.ok(payments);
    }

    /*--------- 13. Registrar una nueva seña/pago (ADMIN o RECEPCIONIST) ---------------*/
    @PostMapping("/payments")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST')")
    @Operation(summary = "Registrar seña o pago", description = "Agrega un pago a la cuenta financiera de la reserva.")
    public ResponseEntity<PaymentDTOResponse> addPaymentToBooking(@Valid @RequestBody PaymentDTORequest request) {
        PaymentDTOResponse newPayment = bookingService.addPaymentToBooking(request);
        return new ResponseEntity<>(newPayment, HttpStatus.CREATED);
    }

    /*--------- 14. Listar reservas por estado específico (ADMIN o RECEPCIONIST) ---------------*/
    @GetMapping("/state/{state}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONIST')")
    @Operation(summary = "Listar reservas por estado de reserva", description = "Filtra las reservas según su BookingState (PENDING, CONFIRMED, CHECKED_IN, etc.)")
    public ResponseEntity<List<BookingDTOResponse>> getBookingsByState(@PathVariable BookingState state) {
        List<BookingDTOResponse> bookings = bookingService.getBookingsByState(state);
        return ResponseEntity.ok(bookings);
    }

}