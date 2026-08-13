package com.example.ultimate_hotel_software_v30.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Room state for the system")
public enum RoomState {

    @Schema(description = "Available room for check in")
    AVAILABLE,

    @Schema(description = "Room not available for check in")
    OCCUPIED,

    @Schema(description = "Room not available")
    MAINTENANCE,

    @Schema(description = "Room under cleaning tasks")
    CLEANING;

}
