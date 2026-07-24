package com.jhonatan.ecommerce_api.dto.pedido;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record PedidoRequestDTO(
        @Valid // não é obrigatório em toda lista, ele é necessário quando a lista contém objetos que também possuem validações.
        @NotEmpty // Não vai ser nulo e tem que ser maior que zero.
        List<ItemPedidoRequestDTO> itensDoPedido
) {
}
