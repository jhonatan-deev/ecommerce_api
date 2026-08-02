package com.jhonatan.ecommerce_api.dto.dois_fatores;

import jakarta.validation.constraints.NotBlank;

public record DesativarDoisFatoresDTO(
        @NotBlank
        String senhaAtual
) {
}
