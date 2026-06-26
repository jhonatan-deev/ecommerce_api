package com.jhonatan.ecommerce_api.exception;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String message) {
        super(message);
    }
}
