package com.jhonatan.ecommerce_api.dto.categoria;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String nome,
        @NotBlank(message = "Descrição é obrigatório")
        String descricao) {
}
