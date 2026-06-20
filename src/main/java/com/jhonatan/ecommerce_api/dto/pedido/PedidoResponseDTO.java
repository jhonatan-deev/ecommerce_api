package com.jhonatan.ecommerce_api.dto.pedido;

import com.jhonatan.ecommerce_api.dto.usuario.UsuarioResponseDTO;
import com.jhonatan.ecommerce_api.enums.StatusPedido;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PedidoResponseDTO(
        Long id,
        UsuarioResponseDTO usuario,
        LocalDate data,
        StatusPedido statusDePagamento,
        BigDecimal valorTotalPedido,
        List<ItemPedidoResponseDTO> itens
) {
}