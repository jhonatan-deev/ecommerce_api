package com.jhonatan.ecommerce_api.dto.usuario;

import com.jhonatan.ecommerce_api.enums.TipoUsuario;

public record UsuarioUpdateDTO(
        String nome,
        String email,
        String senha,
        TipoUsuario tipo
) {
}
