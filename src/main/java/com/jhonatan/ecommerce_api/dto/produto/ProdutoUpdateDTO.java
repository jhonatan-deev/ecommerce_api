package com.jhonatan.ecommerce_api.dto.produto;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ProdutoUpdateDTO(
        String nome,
        String descricao,
        BigDecimal preco,
        @Positive Integer quantidadeEntrada,
        Long categoriaId
) {}