package com.jhonatan.ecommerce_api.dto.produto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProdutoRequestDTO(
        @NotBlank
        String nome,
        @NotBlank
        String descricao,
        @NotNull
        @Positive
        BigDecimal preco,
        @NotNull
        @PositiveOrZero
        Integer estoque,
        @NotNull
        Long categoriaId
) {
}
