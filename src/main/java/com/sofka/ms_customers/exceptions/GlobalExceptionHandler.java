package com.sofka.ms_customers.exceptions;

import com.sofka.ms_customers.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(ex.getMessage(), null));
    }

    @ExceptionHandler(InvalidCustomerDataException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidCustomerDataException(InvalidCustomerDataException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(ex.getMessage(), null));
    }
}
