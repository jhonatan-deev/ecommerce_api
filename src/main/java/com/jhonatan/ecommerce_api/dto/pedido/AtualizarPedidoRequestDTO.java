package com.jhonatan.ecommerce_api.dto.pedido;

import com.jhonatan.ecommerce_api.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record AtualizarPedidoRequestDTO(@NotNull StatusPedido novoStatus) {
}
