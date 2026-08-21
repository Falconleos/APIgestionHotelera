package com.example.ultimate_hotel_software_v30.service.serviceImpl;

import com.example.ultimate_hotel_software_v30.dto.request.BookingCancellationDTORequest;
import com.example.ultimate_hotel_software_v30.dto.request.BookingDTORequest;
import com.example.ultimate_hotel_software_v30.dto.request.PaymentDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.BookingCancellationDTOResponse;
import com.example.ultimate_hotel_software_v30.dto.response.BookingDTOResponse;
import com.example.ultimate_hotel_software_v30.dto.response.PaymentDTOResponse;
import com.example.ultimate_hotel_software_v30.dto.response.RoomDTOResponse;
import com.example.ultimate_hotel_software_v30.enums.BookingState;
import com.example.ultimate_hotel_software_v30.exceptions.*;
import com.example.ultimate_hotel_software_v30.mapper.BookingCancellationMapper;
import com.example.ultimate_hotel_software_v30.mapper.BookingMapper;
import com.example.ultimate_hotel_software_v30.mapper.PaymentMapper; // Asegúrate de tener tu mapper de pagos o ajustarlo según tu estructura
import com.example.ultimate_hotel_software_v30.mapper.RoomMapper;
import com.example.ultimate_hotel_software_v30.model.*;
import com.example.ultimate_hotel_software_v30.repository.*;
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

    private final CreditNoteService creditNoteService;

    // Repositorio de cuentas agregado para la creación automática
    private final AccountRepository accountRepository;

    // Repositorio y Mapper de pagos (ajusta el nombre según tus clases existentes)
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

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

        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        String currentUsername = "";
        if (principal instanceof UserDetails) {
            currentUsername = ((UserDetails) principal).getUsername();
        }

        UserEntity currentUser = userService.findEntityByUsername(currentUsername);

        if (!currentUser.isEnabled()) {
            throw new DisabledUserException("A disabled user cannot generate a booking.");
        }

        BookingEntity booking = bookingMapper.toBookingEntity(request);

        if (request.getUserId() != null) {
            UserEntity requestingUser = userService.findEntityById(request.getUserId());

            if (!requestingUser.isEnabled()) {
                throw new DisabledUserException("The user requesting the booking is disabled.");
            }

            booking.setUserEntity(requestingUser);

            if (requestingUser.getPhoneNumber() != null && !requestingUser.getPhoneNumber().isEmpty()) {
                booking.setGuestPhone(requestingUser.getPhoneNumber());
            }
        } else {
            booking.setUserEntity(null);
        }

        booking.setUserBookingEntity(currentUser);
        booking.setRoomEntity(room);
        booking.setTotalPrice(totalPrice);
        booking.setActive(true);
        booking.setState(BookingState.PENDING);
        booking.setCreatedAt(LocalDateTime.now());

        BookingEntity savedBooking = bookingRepository.save(booking);

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

        booking.setState(BookingState.CANCELLED);
        booking.setActive(false);
        bookingRepository.save(booking);

        AccountEntity account = accountRepository.findByBookingEntity_Id(booking.getId())
                .orElseThrow(() -> new InvalidIdException("Account not found for booking ID: " + booking.getId()));

        if (account.getPaidAmount() > 0) {
            creditNoteService.createCreditNote(account, request.getReason());
            account.setState(com.example.ultimate_hotel_software_v30.enums.AccountState.REFUNDED);
        } else {
            account.setState(com.example.ultimate_hotel_software_v30.enums.AccountState.CANCELLED);
        }
        accountRepository.save(account);

        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        String currentUsername = "";
        if (principal instanceof UserDetails) {
            currentUsername = ((UserDetails) principal).getUsername();
        }

        UserEntity currentUser = userService.findEntityByUsername(currentUsername);

        if (!currentUser.isEnabled()) {
            throw new DisabledUserException("A disabled user cannot generate a cancellation.");
        }

        BookingCancellationEntity cancellation = BookingCancellationEntity.builder()
                .userEntity(currentUser)
                .booking(booking)
                .reason(request.getReason())
                .build();

        BookingCancellationEntity savedCancellation = bookingCancellationService.create(cancellation);
        return bookingCancellationMapper.toBookingCancellationDTOResponse(savedCancellation);
    }

    @Override
    @Transactional
    public void update(BookingEntity booking) {
        bookingRepository.save(booking);
    }

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

    @Override
    @Transactional(readOnly = true)
    public List<BookingDTOResponse> getCheckInsOfToday() {
        return bookingRepository.findByActive(true).stream()
                .filter(b -> b.getCheckIn().equals(LocalDate.now()))
                .map(bookingMapper::toBookingDTOResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDTOResponse> getBookingsToConfirmInDays(Integer days) {
        return bookingRepository.findByActive(true).stream()
                .filter(b -> b.getState() == BookingState.PENDING)
                .filter(b -> LocalDate.now().plusDays(days).isEqual(b.getCheckIn()))
                .map(bookingMapper::toBookingDTOResponse)
                .toList();
    }

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

    @Override
    @Transactional(readOnly = true)
    public boolean existsByRoomId(Long roomId) {
        return bookingRepository.existsByRoomEntity_Id(roomId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDTOResponse> getBookingsByState(BookingState state) {
        return bookingRepository.findByState(state).stream()
                .map(bookingMapper::toBookingDTOResponse)
                .toList();
    }

    @Override
    @Transactional
    public BookingDTOResponse checkInBooking(Long bookingId, String dni) {
        BookingEntity booking = findEntityById(bookingId);

        if (booking.getState() == BookingState.CHECKED_IN) {
            throw new BookingStateConflictException("This booking is already checked-in.");
        }
        if (booking.getState() == BookingState.CANCELLED ||
                booking.getState() == BookingState.NO_SHOW ||
                booking.getState() == BookingState.CONCLUDED ||
                booking.getState() == BookingState.INTERRUPTED) {
            throw new BookingStateConflictException("Cannot perform check-in for a booking in state: " + booking.getState());
        }

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

        booking.setUserEntity(user);
        booking.setState(BookingState.CHECKED_IN);

        BookingEntity savedBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingDTOResponse(savedBooking);
    }

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

    // --- IMPLEMENTACIÓN DE LOS MÉTODOS DE PAGOS / SEÑAS ---

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDTOResponse> getPaymentsByBookingId(Long bookingId) {
        AccountEntity account = accountRepository.findByBookingEntity_Id(bookingId)
                .orElseThrow(() -> new InvalidIdException("Account not found for booking ID: " + bookingId));

        List<PaymentEntity> payments = paymentRepository.findByAccountEntity_Id(account.getId());

        return payments.stream().map(payment -> {
            // 1. Mapeamos el pago base
            PaymentDTOResponse response = paymentMapper.toPaymentDTOResponse(payment);
            response.setAccountId(account.getId());

            // 2. Si el pago tiene un ID de usuario asociado en la base de datos...
            if (payment.getUserEntity() != null && payment.getUserEntity().getId() != null) {
                Long userId = payment.getUserEntity().getId();
                response.setUserId(userId);

                // 3. Lo buscamos explícitamente y extraemos los campos que necesita el DTO
                try {
                    UserEntity user = userRepository.findById(userId).orElse(null);
                    if (user != null) {
                        response.setUsername(user.getUsername());
                        // Si tu DTO también requiere nombre y apellido, los seteas aquí:
                        // response.setRegisteredByName(user.getName());
                        // response.setRegisteredBySurname(user.getSurname());
                    }
                } catch (Exception e) {
                    // Evitamos que falle todo el listado si un usuario llegara a dar error
                    System.err.println("No se pudo obtener el usuario con ID: " + userId);
                }
            }

            return response;
        }).toList();
    }

    @Override
    @Transactional
    public PaymentDTOResponse addPaymentToBooking(PaymentDTORequest request) {
        // 1. Buscar la cuenta financiera directamente por su ID que viene en el request
        AccountEntity account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new InvalidIdException("Account not found with ID: " + request.getAccountId()));

        // 2. Obtener el usuario autenticado que está registrando el pago
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        String currentUsername = "";
        if (principal instanceof UserDetails) {
            currentUsername = ((UserDetails) principal).getUsername();
        }

        UserEntity currentUser = userService.findEntityByUsername(currentUsername);

        if (!currentUser.isEnabled()) {
            throw new DisabledUserException("A disabled user cannot register a payment.");
        }

        // 3. Crear la entidad de pago vinculada a la cuenta y al usuario actual
        PaymentEntity payment = PaymentEntity.builder()
                .accountEntity(account)
                .amount(request.getAmount())
                .paymentMethod(com.example.ultimate_hotel_software_v30.enums.PaymentMethod.valueOf(request.getPaymentMethod()))
                .transactionReference(request.getTransactionReference())
                .paymentDate(LocalDateTime.now())
                .userEntity(currentUser)
                .build();

        PaymentEntity savedPayment = paymentRepository.save(payment);

        // 4. Actualizar el monto pagado (paidAmount) en la cuenta financiera
        account.setPaidAmount(account.getPaidAmount() + request.getAmount());

        // Verificar si la cuenta ya está totalmente pagada
        double totalToPay = account.getBaseAmount() + account.getServicesTotal();
        if (account.getPaidAmount() >= totalToPay) {
            account.setIsPaid(true);
        }

        accountRepository.save(account);

        // 5. Mapear la respuesta incluyendo los datos del usuario y la cuenta
        PaymentDTOResponse response = paymentMapper.toPaymentDTOResponse(savedPayment);
        response.setAccountId(account.getId());
        response.setUserId(currentUser.getId());
        response.setUsername(currentUser.getUsername());

        return response;
    }
}