package com.example.ultimate_hotel_software_v30.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditNoteDTOResponse {
    private Long id;
    private Long accountId;
    private Double amount;
    private String reason;
    private LocalDateTime issuedAt;
}