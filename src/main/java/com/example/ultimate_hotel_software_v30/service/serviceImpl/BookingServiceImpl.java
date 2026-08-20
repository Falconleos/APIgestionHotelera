package com.example.ultimate_hotel_software_v30.service.serviceImpl;

import com.example.ultimate_hotel_software_v30.dto.request.BookingCancellationDTORequest;
import com.example.ultimate_hotel_software_v30.dto.request.BookingDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.BookingCancellationDTOResponse;
import com.example.ultimate_hotel_software_v30.dto.response.BookingDTOResponse;
import com.example.ultimate_hotel_software_v30.dto.response.RoomDTOResponse;
import com.example.ultimate_hotel_software_v30.dto.response.UserDTOResponse;
import com.example.ultimate_hotel_software_v30.enums.BookingState;
import com.example.ultimate_hotel_software_v30.exceptions.*;
import com.example.ultimate_hotel_software_v30.mapper.BookingCancellationMapper;
import com.example.ultimate_hotel_software_v30.mapper.BookingMapper;
import com.example.ultimate_hotel_software_v30.mapper.RoomMapper;
import com.example.ultimate_hotel_software_v30.model.*;
import com.example.ultimate_hotel_software_v30.repository.AccountRepository;
import com.example.ultimate_hotel_software_v30.repository.BookingCancellationRepository;
import com.example.ultimate_hotel_software_v30.repository.BookingRepository;
import com.example.ultimate_hotel_software_v30.repository.UserRepository;
import com.example.ultimate_hotel_software_v30.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingCancellationRepository bookingCancellationRepository;
    private final BookingMapper bookingMapper;

    private final BookingCancellationMapper bookingCancellationMapper;
    private final BookingCancellationService bookingCancellationService;

    private final RoomService roomService;
    private final RoomMapper roomMapper;

    private final UserService userService;
    private final UserRepository userRepository;
    private final EmployeeService employeeService;

    // Repositorio de cuentas agregado para la creación automática
    private final AccountRepository accountRepository;

    // 1. Busqueda por ID
    @Override
    @Transactional(readOnly = true)
    public BookingEntity findEntityById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDTOResponse findById(Long id) {
        return bookingMapper.toBookingDTOResponse(findEntityById(id));
    }

    // 2. Listar reservas activas o inactivas
    @Override
    @Transactional(readOnly = true)
    public List<BookingDTOResponse> getBookings(Boolean active) {
        List<BookingEntity> bookings;
        if (active == null) {
            bookings = bookingRepository.findAll();
        } else {
            bookings = bookingRepository.findByActive(active);
        }
        return bookings.stream()
                .map(bookingMapper::toBookingDTOResponse)
                .toList();
    }

    // 3. Crear reserva
    @Override
    @Transactional
    public BookingDTOResponse createBooking(BookingDTORequest request) {
        if (!request.getCheckOut().isAfter(request.getCheckIn())) {
            throw new InvalidDateException("The check-out date must be after the check-in date.");
        }

        RoomEntity room = roomService.findEntityById(request.getRoomId());

        if (room.getType().getCapacity() < request.getGuestCount()) {
            throw new CapacityOutOfRangeException("Room capacity exceeded for the requested guest count.");
        }

        if (!getAvailableRoomsEntities(request.getCheckIn(), request.getCheckOut(), request.getGuestCount()).contains(room)) {
            throw new DisabledRoomException("The selected room is not available for these dates.");
        }

        long days = ChronoUnit.DAYS.between(request.getCheckIn(), request.getCheckOut());
        double totalPrice = room.getType().getPricePerNight() * days;

        // 1. Obtener el empleado que está tomando/registrando la reserva (vía SecurityContext)
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        String employeeUsername = "";
        if (principal instanceof UserDetails) {
            employeeUsername = ((UserDetails) principal).getUsername();
        }

        UserDTOResponse userDtoResponse = userService.findByUsername(employeeUsername);
        EmployeeEntity employee = employeeService.findEntityById(userDtoResponse.getId());

        if (employee.getUserEntity() != null && !employee.getUserEntity().isEnabled()) {
            throw new DisabledUserException("A disabled employee cannot generate a booking.");
        }

        // 2. Mapeo inicial de la entidad desde el request
        BookingEntity booking = bookingMapper.toBookingEntity(request);

        // 3. Gestionar el usuario solicitante opcional (si viene un userId en el request)
        if (request.getUserId() != null) {
            UserEntity requestingUser = userService.findEntityById(request.getUserId());

            if (!requestingUser.isEnabled()) {
                throw new DisabledUserException("The user requesting the booking is disabled.");
            }

            booking.setUserEntity(requestingUser); // Vinculamos el usuario registrado si existe
        } else {
            booking.setUserEntity(null); // Es una reserva telefónica / rápida sin cuenta de usuario asociada
        }

        // 4. Asignaciones restantes y guardado de la reserva
        booking.setEmployeeBookingEntity(employee);
        booking.setRoomEntity(room);
        booking.setTotalPrice(totalPrice);
        booking.setActive(true);
        booking.setState(BookingState.PENDING);
        booking.setCreatedAt(LocalDateTime.now());

        BookingEntity savedBooking = bookingRepository.save(booking);

        // 5. Creación automática de la cuenta financiera asociada a la reserva
        AccountEntity account = AccountEntity.builder()
                .bookingEntity(savedBooking)
                .baseAmount(totalPrice)
                .servicesTotal(0.0)
                .paidAmount(0.0)
                .isPaid(false)
                .adjustmentPercentage(0)
                .build();

        accountRepository.save(account);

        return bookingMapper.toBookingDTOResponse(savedBooking);
    }

    // 3.2. Cancelar una reserva
    @Override
    @Transactional
    public BookingCancellationDTOResponse cancelBooking(BookingCancellationDTORequest request) {
        BookingEntity booking = findEntityById(request.getBookingId());

        if (booking.getState() == BookingState.CHECKED_IN) {
            throw new BookingStateConflictException("The guest has already checked in. You can only interrupt the stay.");
        }
        if (booking.getState() == BookingState.NO_SHOW) {
            throw new BookingStateConflictException("This booking is already inactive due to a No-Show.");
        }
        if (booking.getState() == BookingState.CONCLUDED) {
            throw new BookingStateConflictException("Cannot cancel a concluded booking.");
        }
        if (booking.getState() == BookingState.CANCELLED) {
            throw new BookingStateConflictException("This booking is already cancelled.");
        }

        if (bookingCancellationRepository.existsByBookingId(booking.getId())) {
            throw new BookingStateConflictException("Esta reserva ya cuenta con un registro físico de cancelación en el sistema.");
        }

        // Actualizamos estado de la reserva
        booking.setState(BookingState.CANCELLED);
        booking.setActive(false);
        bookingRepository.save(booking);

        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        String employeeUsername = "";

        if (principal instanceof UserDetails) {
            employeeUsername = ((UserDetails) principal).getUsername();
        }

        var userDtoResponse = userService.findByUsername(employeeUsername);
        EmployeeEntity employee = employeeService.findEntityById(userDtoResponse.getId());

        if (employee.getUserEntity() != null && !employee.getUserEntity().isEnabled()) {
            throw new DisabledUserException("A disabled employee cannot generate a cancellation.");
        }

        BookingCancellationEntity cancellation = BookingCancellationEntity.builder()
                .employee(employee)
                .booking(booking)
                .reason(request.getReason())
                .build();

        BookingCancellationEntity savedCancellation = bookingCancellationService.create(cancellation);
        return bookingCancellationMapper.toBookingCancellationDTOResponse(savedCancellation);
    }

    // 5. Actualizar reserva genérica en el repositorio
    @Override
    @Transactional
    public void update(BookingEntity booking) {
        bookingRepository.save(booking);
    }

    // 5.2. Confirmar reserva (De PENDING a CONFIRMED)
    @Override
    @Transactional
    public BookingDTOResponse confirmBooking(Long id) {
        BookingEntity booking = findEntityById(id);
        if (booking.getState() != BookingState.PENDING) {
            throw new BookingStateConflictException("Current booking state is: " + booking.getState() + ". To confirm, it must be PENDING.");
        }
        booking.setState(BookingState.CONFIRMED);
        return bookingMapper.toBookingDTOResponse(bookingRepository.save(booking));
    }

    // 6. Consultas de disponibilidad y listados especializados
    @Override
    @Transactional(readOnly = true)
    public List<RoomEntity> getAvailableRoomsEntities(LocalDate checkIn, LocalDate checkOut, Integer guestCount) {
        List<BookingEntity> allBookings = bookingRepository.findAll();

        List<Long> occupiedRoomIds = allBookings.stream()
                .filter(b -> Boolean.TRUE.equals(b.getActive()))
                .filter(b -> b.getState() != BookingState.CANCELLED &&
                        b.getState() != BookingState.NO_SHOW &&
                        b.getState() != BookingState.INTERRUPTED &&
                        b.getState() != BookingState.CONCLUDED)
                .filter(b -> checkIn.isBefore(b.getCheckOut()) && checkOut.isAfter(b.getCheckIn()))
                .map(b -> b.getRoomEntity().getId())
                .distinct()
                .toList();

        return roomService.findAll().stream()
                .filter(r -> r.getType().getCapacity() >= guestCount)
                .filter(r -> !occupiedRoomIds.contains(r.getId()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDTOResponse> getAvailableRooms(LocalDate checkIn, LocalDate checkOut, Integer guestCount) {
        List<RoomEntity> availableRooms = getAvailableRoomsEntities(checkIn, checkOut, guestCount);
        return availableRooms.stream()
                .map(roomMapper::toRoomDTOResponse)
                .toList();
    }

    // 6.2. Check-Ins programados para hoy
    @Override
    @Transactional(readOnly = true)
    public List<BookingDTOResponse> getCheckInsOfToday() {
        return bookingRepository.findByActive(true).stream()
                .filter(b -> b.getCheckIn().equals(LocalDate.now()))
                .map(bookingMapper::toBookingDTOResponse)
                .toList();
    }

    // 6.3. Reservas activas PENDING a X días del check-in
    @Override
    @Transactional(readOnly = true)
    public List<BookingDTOResponse> getBookingsToConfirmInDays(Integer days) {
        return bookingRepository.findByActive(true).stream()
                .filter(b -> b.getState() == BookingState.PENDING)
                .filter(b -> LocalDate.now().plusDays(days).isEqual(b.getCheckIn()))
                .map(bookingMapper::toBookingDTOResponse)
                .toList();
    }

    // 7. Procesamiento automatizado de Ausencias (No-Show)
    @Override
    @Transactional
    public void processNoShowBookings() {
        LocalDate today = LocalDate.now();
        List<BookingEntity> noShows = bookingRepository.findByActive(true).stream()
                .filter(b -> b.getCheckIn().isBefore(today))
                .filter(b -> b.getState() == BookingState.PENDING || b.getState() == BookingState.CONFIRMED)
                .toList();

        for (BookingEntity booking : noShows) {
            booking.setState(BookingState.NO_SHOW);
            booking.setActive(false);
            bookingRepository.save(booking);
        }
    }

    // 7.2. Validar si existen reservas asociadas a una habitación física
    @Override
    @Transactional(readOnly = true)
    public boolean existsByRoomId(Long roomId) {
        return bookingRepository.existsByRoomEntity_Id(roomId);
    }

    // 8. Filtrar por estado de reserva
    @Override
    @Transactional(readOnly = true)
    public List<BookingDTOResponse> getBookingsByState(BookingState state) {
        return bookingRepository.findByState(state).stream()
                .map(bookingMapper::toBookingDTOResponse)
                .toList();
    }

    // 9. Realizar Check-In validando que el usuario por DNI ya exista
    @Override
    @Transactional
    public BookingDTOResponse checkInBooking(Long bookingId, String dni) {
        BookingEntity booking = findEntityById(bookingId);

        // 1. Validaciones de estado previas
        if (booking.getState() == BookingState.CHECKED_IN) {
            throw new BookingStateConflictException("This booking is already checked-in.");
        }
        if (booking.getState() == BookingState.CANCELLED ||
                booking.getState() == BookingState.NO_SHOW ||
                booking.getState() == BookingState.CONCLUDED ||
                booking.getState() == BookingState.INTERRUPTED) {
            throw new BookingStateConflictException("Cannot perform check-in for a booking in state: " + booking.getState());
        }

        // 2. Verificar si la reserva ya tiene un usuario vinculado o si debemos buscarlo por DNI
        UserEntity user = null;

        if (dni != null && !dni.isBlank()) {
            user = userRepository.findByDni(dni)
                    .orElseThrow(() -> new UserNotFoundException("No user found with DNI: " + dni + ". Please register the user first."));
        } else if (booking.getUserEntity() != null) {
            user = booking.getUserEntity();
        } else {
            throw new UserNotFoundException("This booking has no user associated and no DNI was provided. Please link a user first.");
        }

        if (!user.isEnabled()) {
            throw new DisabledUserException("The user associated with this DNI is disabled.");
        }

        // 3. Vincular (por seguridad) y actualizar el estado a CHECKED_IN
        booking.setUserEntity(user);
        booking.setState(BookingState.CHECKED_IN);

        BookingEntity savedBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingDTOResponse(savedBooking);
    }

    // 10. Método independiente para asociar un usuario existente a una reserva
    @Override
    @Transactional
    public BookingDTOResponse assignUserToBooking(Long bookingId, Long userId) {
        BookingEntity booking = findEntityById(bookingId);
        UserEntity user = userService.findEntityById(userId);

        if (!user.isEnabled()) {
            throw new DisabledUserException("Cannot assign a disabled user to a booking.");
        }

        booking.setUserEntity(user);
        BookingEntity savedBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingDTOResponse(savedBooking);
    }

}