package com.example.ultimate_hotel_software_v30.service.serviceImpl;

import com.example.ultimate_hotel_software_v30.dto.response.BookingCancellationDTOResponse;
import com.example.ultimate_hotel_software_v30.exceptions.InvalidIdException;
import com.example.ultimate_hotel_software_v30.mapper.BookingCancellationMapper;
import com.example.ultimate_hotel_software_v30.model.BookingCancellationEntity;
import com.example.ultimate_hotel_software_v30.repository.BookingCancellationRepository;
import com.example.ultimate_hotel_software_v30.service.BookingCancellationService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingCancellationServiceImpl implements BookingCancellationService {

    private final BookingCancellationRepository repository;
    private final BookingCancellationMapper mapper;

    // 1. Búsqueda por ID
    // 1.1. Devuelve entidad
    @Override
    @Transactional(readOnly = true)
    public BookingCancellationEntity findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new InvalidIdException("Invalid cancellation ID: " + id));
    }

    // 1.2. Devuelve DTOResponse
    @Override
    @Transactional(readOnly = true)
    public BookingCancellationDTOResponse findById(Long id) {
        return mapper.toBookingCancellationDTOResponse(findEntityById(id));
    }

    // 2. Listar cancelaciones
    // 2.1. Listar historial completo de cancelaciones
    @Override
    @Transactional(readOnly = true)
    public List<BookingCancellationDTOResponse> getCancellationHistory() {
        List<BookingCancellationEntity> cancellations = repository.findAll();
        return cancellations.stream()
                .map(mapper::toBookingCancellationDTOResponse)
                .toList();
    }

    // 2.2. Listar cancelaciones filtrando por el apellido del huésped principal
    @Override
    @Transactional(readOnly = true)
    public List<BookingCancellationDTOResponse> findByGuestLastName(String lastName) {
        List<BookingCancellationEntity> cancellations = repository.findAll();
        return cancellations.stream()
                .filter(c -> c.getBooking().getGuestLastName().toLowerCase().contains(lastName.toLowerCase()))
                .map(mapper::toBookingCancellationDTOResponse)
                .toList();
    }

    // 3. Crear registro de cancelación física
    @Override
    @Transactional
    public BookingCancellationEntity create(BookingCancellationEntity cancellation) {
        if (cancellation.getBooking() != null && cancellation.getBooking().getId() != null) {
            Long bookingId = cancellation.getBooking().getId();

            // Verificamos directamente en la BD si ya existe una cancelación para esta reserva
            if (repository.existsByBookingId(bookingId)) {
                throw new IllegalStateException("La reserva con ID " + bookingId + " ya se encuentra cancelada.");
            }
        }

        cancellation.setCancellationDate(LocalDateTime.now());

        try {
            return repository.save(cancellation);
        } catch (DataIntegrityViolationException e) {
            // Captura defensiva por si la base de datos tiene un índice único duplicado
            Long bookingId = cancellation.getBooking() != null ? cancellation.getBooking().getId() : null;
            throw new IllegalStateException("Ya existe un registro físico de cancelación para la reserva con ID " + bookingId + ".");
        }
    }

    // 4. Depurar historial de cancelaciones de más de un mes de antigüedad
    @Override
    @Transactional
    public void purgeCancellationHistory() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        List<BookingCancellationEntity> oldCancellations = repository.findAll().stream()
                .filter(c -> c.getCancellationDate().isBefore(oneMonthAgo))
                .toList();

        repository.deleteAllInBatch(oldCancellations);
    }

}