package com.bank.account.controllers;

import com.bank.account.exceptions.CustomerAlreadyExistsException;
import com.bank.account.exceptions.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<String> handleCustomerNotFoundException(CustomerAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}
