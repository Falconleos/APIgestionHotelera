package com.example.ultimate_hotel_software_v30.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "room_types")
public class RoomTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true) // Agregado unique para consistencia
    private String name;

    @Positive(message = "only positive number")
    private Integer capacity;

    @Column(nullable = false, length = 250)
    private String description;

    @Column(nullable = false)
    private Double pricePerNight;

    @OneToMany(mappedBy = "type", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    private List<RoomEntity> rooms = new ArrayList<>();

}