package com.example.ultimate_hotel_software_v30.model;

import com.example.ultimate_hotel_software_v30.enums.AccountState;
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
    @OneToMany(mappedBy = "accountEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentEntity> payments = new ArrayList<>();

    // Relación con la Nota de Crédito en caso de reembolso/cancelación
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private CreditNoteEntity creditNote;

    // Estado actual de la cuenta
    @NotNull
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false, length = 20)
    private AccountState state = AccountState.OPEN;

    // 1. Estadía base fija
    @NotNull
    @Column(nullable = false)
    private Double baseAmount;

    // 2. Subtotal aislado para consumos/servicios extra
    @NotNull
    @Builder.Default
    @Column(nullable = false)
    private Double servicesTotal = 0.0;

    // 3. Suma acumulada de los pagos
    @NotNull
    @Builder.Default
    @Column(nullable = false)
    private Double paidAmount = 0.0;

    // 4. Se pone en true cuando paidAmount >= total final
    @NotNull
    @Builder.Default
    @Column(nullable = false)
    private Boolean isPaid = false;

    // 5. Porcentaje de recargo (+) o descuento (-)
    @Builder.Default
    @Column(nullable = false)
    private Integer adjustmentPercentage = 0;
}