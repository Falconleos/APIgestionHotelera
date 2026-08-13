package com.example.ultimate_hotel_software_v30.exceptions;

public class DisabledUserException extends RuntimeException {
    public DisabledUserException(String message) {
        super(message);
    }
}
