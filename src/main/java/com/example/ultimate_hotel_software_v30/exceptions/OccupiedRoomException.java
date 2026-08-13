package com.example.ultimate_hotel_software_v30.exceptions;

public class OccupiedRoomException extends RuntimeException {
    public OccupiedRoomException(String message) {
        super(message);
    }
}
