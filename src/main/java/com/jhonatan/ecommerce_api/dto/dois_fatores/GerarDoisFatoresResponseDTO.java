package com.jhonatan.ecommerce_api.dto.dois_fatores;

public record GerarDoisFatoresResponseDTO(
        String qrCodeBase64,
        String setupToken
) {
}