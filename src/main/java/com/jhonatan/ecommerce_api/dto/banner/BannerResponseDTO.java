package com.jhonatan.ecommerce_api.dto.banner;

public record BannerResponseDTO(
        Long id,
        String titulo,
        String imagemUrl,
        Integer ordem,
        boolean ativo,
        Long categoriaId,
        String categoriaNome
) {
}
