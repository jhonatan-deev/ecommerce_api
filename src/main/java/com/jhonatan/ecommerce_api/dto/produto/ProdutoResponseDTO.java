package com.jhonatan.ecommerce_api.dto.produto;

import java.math.BigDecimal;

public record ProdutoResponseDTO(Long id,
                                 String nome,
                                 String descricao,
                                 BigDecimal preco,
                                 Integer estoque) {
}
