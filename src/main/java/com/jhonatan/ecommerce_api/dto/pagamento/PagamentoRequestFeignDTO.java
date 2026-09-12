package com.jhonatan.ecommerce_api.dto.pagamento;

import java.math.BigDecimal;

public record PagamentoRequestFeignDTO(
        BigDecimal valor,
        String nome,
        String numero,
        String expiracao,
        Long pedidoId,
        Long formaDePagamentoId
) {
}