package com.jhonatan.ecommerce_api.dto.banner;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record BannerRequestDTO(
        @NotBlank String titulo,
        @NotBlank String imagemUrl,
        @NotNull
        @PositiveOrZero
        Integer ordem,
        Long categoriaId
) {
}
