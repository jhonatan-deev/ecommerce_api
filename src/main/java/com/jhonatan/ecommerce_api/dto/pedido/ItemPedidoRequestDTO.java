package com.jhonatan.ecommerce_api.dto.pedido;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemPedidoRequestDTO(
        @NotNull
        Long produtoId,

        @NotNull
        @Positive
        Integer quantidade
) {
}
