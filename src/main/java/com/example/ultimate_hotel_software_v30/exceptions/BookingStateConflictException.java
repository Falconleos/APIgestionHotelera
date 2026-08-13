package com.example.ultimate_hotel_software_v30.exceptions;

public class BookingStateConflictException extends RuntimeException {
    public BookingStateConflictException(String message) {
        super(message);
    }
}
