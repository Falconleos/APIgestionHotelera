package com.example.ultimate_hotel_software_v30.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "room_attentions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomAttentionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private BookingEntity bookingEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;

    @NotNull(message = "Quantity is required")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    @Column(nullable = false)
    private Double unitPrice;

    //Fecha y hora automática de la carga
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    //Empleado que otorgó/cargó el ítem o servicio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now(); // Asigna la fecha y hora exacta al guardar
    }

    public Double getSubtotal() {
        return this.quantity * this.unitPrice;
    }
}