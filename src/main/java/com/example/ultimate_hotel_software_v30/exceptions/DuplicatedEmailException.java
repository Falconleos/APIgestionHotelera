package com.example.ultimate_hotel_software_v30.exceptions;

public class DuplicatedEmailException extends RuntimeException {
    public DuplicatedEmailException(String message) {
        super(message);
    }
}
