package com.example.ultimate_hotel_software_v30.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "booking_cancellations")
public class BookingCancellationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", referencedColumnName = "id", nullable = false, unique = true)
    private BookingEntity booking;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @Column(nullable = false)
    private LocalDateTime cancellationDate;

    @Column(nullable = false, length = 250)
    private String reason;

    @PrePersist
    public void onCreate() {
        if (reason == null || reason.trim().isBlank()) {
            reason = "not specified";
        }
        cancellationDate = LocalDateTime.now();
    }

}