package com.example.ultimate_hotel_software_v30.dto.request;

import lombok.*;

@Data
public class CreditNoteDTORequest {
    private Long accountId;
    private Double amount;
    // El 'reason' lo tomaremos internamente de la reserva, así que no siempre es necesario pasarlo desde el front.
}