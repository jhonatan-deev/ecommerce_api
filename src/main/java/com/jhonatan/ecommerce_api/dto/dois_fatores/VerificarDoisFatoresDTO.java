package com.jhonatan.ecommerce_api.dto.dois_fatores;

import jakarta.validation.constraints.NotBlank;

public record VerificarDoisFatoresDTO(
        @NotBlank
        String tempToken,
        @NotBlank
        String codigo) {
}
