package com.jhonatan.ecommerce_api.dto.produto;

import com.jhonatan.ecommerce_api.dto.categoria.CategoriaResponseDTO;

import java.math.BigDecimal;

public record ProdutoResponseDTO(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        Integer estoque,
        Boolean ativo,
        CategoriaResponseDTO categoria,
        String imagemUrl
) {
}
