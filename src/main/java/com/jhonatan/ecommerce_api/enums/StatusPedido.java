package com.jhonatan.ecommerce_api.enums;

public enum StatusPedido {
    PENDENTE,
    PAGO,
    ENVIADO,
    ENTREGUE,
    CANCELADO;

    public boolean podeTransitarPara(StatusPedido novoStatus) {
        return switch (this) {
            case PENDENTE ->
                    novoStatus == PAGO ||
                            novoStatus == CANCELADO;

            case PAGO ->
                    novoStatus == ENVIADO;

            case ENVIADO ->
                    novoStatus == ENTREGUE;

            case ENTREGUE, CANCELADO ->
                    false;
        };
    }
}