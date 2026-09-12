package com.jhonatan.ecommerce_api.dto.pagamento;

import java.math.BigDecimal;

public record PagamentoResponseDTO(
        Long id,
        BigDecimal valor,
        String nome,
        String numero,
        String expiracao,
        String status,
        Long pedidoId,
        Long formaDePagamentoId
) {
}