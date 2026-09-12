package com.jhonatan.ecommerce_api.dto.pedido;

import com.jhonatan.ecommerce_api.dto.pagamento.PagamentoRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PedidoRequestDTO(
        @NotEmpty
        List<@Valid ItemPedidoRequestDTO> itensDoPedido,
        @Valid
        @NotNull
        PagamentoRequestDTO pagamento
) {
}