package com.jhonatan.ecommerce_api.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        int status,
        String message,
        LocalDateTime timestamp) {

}
