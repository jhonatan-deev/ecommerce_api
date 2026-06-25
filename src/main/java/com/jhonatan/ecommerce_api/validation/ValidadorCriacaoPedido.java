package com.jhonatan.ecommerce_api.validation;

import com.jhonatan.ecommerce_api.dto.pedido.PedidoRequestDTO;

public interface ValidadorCriacaoPedido {
    void validar(PedidoRequestDTO dadosPedido);

}
