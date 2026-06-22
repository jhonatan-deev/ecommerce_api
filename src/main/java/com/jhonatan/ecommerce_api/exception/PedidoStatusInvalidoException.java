package com.jhonatan.ecommerce_api.exception;

public class PedidoStatusInvalidoException extends RuntimeException {
    public PedidoStatusInvalidoException(String mensage) {
        super(mensage);
    }
}
