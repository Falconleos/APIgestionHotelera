package com.example.ultimate_hotel_software_v30.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private BookingEntity bookingEntity;

    @Builder.Default
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentEntity> payments = new ArrayList<>();

    // 1. Estadía base fija (Valor original del check-in, no debe alterarse por actualizacion de precios de las habitaciones)
    @NotNull
    @Column(nullable = false)
    private Double baseAmount;

    // 2. Subtotal aislado para consumos/servicios extra (Room Attention, ítems, etc.)
    @NotNull
    @Builder.Default
    @Column(nullable = false)
    private Double servicesTotal = 0.0;

    //3. Suma acumulada de los pagos
    @NotNull
    @Builder.Default
    @Column(nullable = false)
    private Double paidAmount = 0.0;

    //4. Se pone en true cuando paidAmount >= total final
    @NotNull
    @Builder.Default
    @Column(nullable = false)
    private Boolean isPaid = false;

    //5. Porcentaje de recargo (+) o descuento (-)
    @Builder.Default
    @Column(nullable = false)
    private Integer adjustmentPercentage = 0;


}