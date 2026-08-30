package com.jhonatan.ecommerce_api.mapper;

import com.jhonatan.ecommerce_api.dto.banner.BannerResponseDTO;
import com.jhonatan.ecommerce_api.model.Banner;
import org.springframework.stereotype.Component;

@Component
public class BannerMapper {

    public BannerResponseDTO toResponse(Banner banner) {
        return new BannerResponseDTO(
                banner.getId(),
                banner.getTitulo(),
                banner.getImagemUrl(),
                banner.getOrdem(),
                banner.isAtivo(),
                banner.getCategoria() != null ? banner.getCategoria().getId() : null,
                banner.getCategoria() != null ? banner.getCategoria().getNome() : null
        );
    }
}