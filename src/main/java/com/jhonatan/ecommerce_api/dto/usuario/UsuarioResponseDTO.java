package com.jhonatan.ecommerce_api.dto.usuario;

import com.jhonatan.ecommerce_api.enums.TipoUsuario;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        TipoUsuario tipo
) {
}
