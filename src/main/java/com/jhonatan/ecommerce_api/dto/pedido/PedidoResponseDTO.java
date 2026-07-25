package com.jhonatan.ecommerce_api.dto.pedido;

import com.jhonatan.ecommerce_api.dto.usuario.UsuarioResumoResponseDTO;
import com.jhonatan.ecommerce_api.enums.StatusPedido;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PedidoResponseDTO(
        Long id,
        UsuarioResumoResponseDTO usuarioResumo,
        LocalDate data,
        StatusPedido statusDePagamento,
        BigDecimal valorTotalPedido,
        List<ItemPedidoResponseDTO> itens
) {
}