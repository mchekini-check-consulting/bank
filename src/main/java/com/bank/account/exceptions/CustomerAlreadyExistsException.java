package com.bank.account.exceptions;

public class CustomerAlreadyExistsException extends RuntimeException {
    public CustomerAlreadyExistsException(String message){
        super(message);
    }
}
