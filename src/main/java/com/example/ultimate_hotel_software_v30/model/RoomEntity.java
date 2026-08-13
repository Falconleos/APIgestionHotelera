package com.example.ultimate_hotel_software_v30.model;

import com.example.ultimate_hotel_software_v30.enums.RoomState;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList; // Importa esto
import java.util.List;      // Importa esto

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "rooms")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomTypeEntity type;

    //guardar los bytes directamente en una tabla separada automática:
    @ElementCollection
    @CollectionTable(name = "room_images", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    @Builder.Default
    private List<byte[]> images = new ArrayList<>();
}