package com.sofka.ms_customers.exceptions;

public class DuplicateIdentificationException extends RuntimeException {
    public DuplicateIdentificationException(String message) {
        super(message);
    }
}
