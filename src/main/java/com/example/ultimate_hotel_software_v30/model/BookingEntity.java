package com.example.ultimate_hotel_software_v30.model;

import com.example.ultimate_hotel_software_v30.enums.BookingState;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "bookings")
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate checkIn;

    @Column(nullable = false)
    private LocalDate checkOut;

    @Column(nullable = false)
    private Integer guestCount; // Cantidad de pasajeros (pax)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingState state;

    @Column(nullable = false, length = 100)
    private String guestFirstName;

    @Column(nullable = false, length = 100)
    private String guestLastName;

    @Column(nullable = false, length = 30)
    private String guestPhone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private UserEntity userEntity;

    @Column(length = 64)
    private String qrBooking;

    @Column(length = 250)
    private String observation;

    @Column(nullable = false)
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userBooking_id", nullable = false)
    private UserEntity userBookingEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userCheckIn_id", nullable = true)
    private UserEntity userCheckInEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity roomEntity;

    @Column(nullable = false)
    private Double totalPrice;

    // Relación uno a uno con la cancelación
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BookingCancellationEntity cancellation;

    private LocalDateTime createdAt;

}