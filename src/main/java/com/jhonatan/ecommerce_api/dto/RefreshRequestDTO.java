package com.jhonatan.ecommerce_api.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequestDTO(
        @NotBlank(message = "É necessário fazer login novamente!")
        String refreshToken
) {
}
