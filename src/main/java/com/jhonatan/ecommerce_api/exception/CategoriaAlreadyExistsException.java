package com.jhonatan.ecommerce_api.exception;

public class CategoriaAlreadyExistsException extends RuntimeException {
    public CategoriaAlreadyExistsException(String message) {
        super(message);
    }
}
