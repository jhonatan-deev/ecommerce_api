package com.jhonatan.ecommerce_api.exception;

public class IdProdutoNotFoundException extends RuntimeException {
    public IdProdutoNotFoundException(String message) {
        super(message);
    }
}
