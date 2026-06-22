package com.jhonatan.ecommerce_api.enums;

public enum StatusPedido {
    PENDENTE,
    AGUARDANDO_PAGAMENTO,
    PAGO,
    ENVIADO,
    ENTREGUE,
    CANCELADO;

    public boolean podeTransitarPara(StatusPedido novoStatus) {
        return switch (this) {
            case PENDENTE -> novoStatus == AGUARDANDO_PAGAMENTO
                    || novoStatus == CANCELADO; // Pode cancelar antes de pagar

            case AGUARDANDO_PAGAMENTO -> novoStatus == PAGO
                    || novoStatus == CANCELADO; // Pode cancelar se o pagamento falhar

            case PAGO -> novoStatus == ENVIADO;
            // (Opcional: se a loja permitir estorno, adicione || novoStatus == CANCELADO)

            case ENVIADO -> novoStatus == ENTREGUE; // Só avança após entregue

            // Estados finais (não vão para lugar nenhum)
            case ENTREGUE, CANCELADO -> false;
        };
    }
}
