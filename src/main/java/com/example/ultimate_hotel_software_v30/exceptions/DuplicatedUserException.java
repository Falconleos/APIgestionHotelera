package com.example.ultimate_hotel_software_v30.exceptions;

public class DuplicatedUserException extends RuntimeException {
    public DuplicatedUserException(String message) {
        super(message);
    }
}
