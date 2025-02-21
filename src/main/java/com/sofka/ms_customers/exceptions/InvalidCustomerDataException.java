package com.sofka.ms_customers.exceptions;

public class InvalidCustomerDataException extends RuntimeException {
    public InvalidCustomerDataException(String message) {
        super(message);
    }
}
