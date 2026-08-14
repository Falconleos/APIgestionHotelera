package com.example.ultimate_hotel_software_v30.service;

import com.example.ultimate_hotel_software_v30.dto.request.BookingCancellationDTORequest;
import com.example.ultimate_hotel_software_v30.dto.request.BookingDTORequest;
import com.example.ultimate_hotel_software_v30.dto.response.BookingCancellationDTOResponse;
import com.example.ultimate_hotel_software_v30.dto.response.BookingDTOResponse;
import com.example.ultimate_hotel_software_v30.dto.response.RoomDTOResponse;
import com.example.ultimate_hotel_software_v30.enums.BookingState;
import com.example.ultimate_hotel_software_v30.model.BookingEntity;
import com.example.ultimate_hotel_software_v30.model.RoomEntity;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    BookingEntity findEntityById(Long id);

    BookingDTOResponse findById(Long id);

    List<BookingDTOResponse> getBookings(Boolean active);

    BookingDTOResponse createBooking(BookingDTORequest request);

    BookingCancellationDTOResponse cancelBooking(BookingCancellationDTORequest request);

    void update(BookingEntity booking);

    BookingDTOResponse confirmBooking(Long id);

    List<RoomEntity> getAvailableRoomsEntities(LocalDate checkIn, LocalDate checkOut, Integer guestCount);

    List<RoomDTOResponse> getAvailableRooms(LocalDate checkIn, LocalDate checkOut, Integer guestCount);

    List<BookingDTOResponse> getCheckInsOfToday();

    List<BookingDTOResponse> getBookingsToConfirmInDays(Integer days);

    void processNoShowBookings();

    boolean existsByRoomId(Long roomId);

    List<BookingDTOResponse> getBookingsByState(BookingState state);

    BookingDTOResponse checkInBooking(Long bookingId, String dni);

    BookingDTOResponse assignUserToBooking(Long bookingId, Long userId);

}