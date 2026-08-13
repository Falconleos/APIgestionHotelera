package com.example.ultimate_hotel_software_v30.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoomAttentionDTORequest {

    @NotNull(message = "Check-in ID is required")
    private Long bookingId;

    @NotNull(message = "Item ID is required")
    private Long itemId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

}
