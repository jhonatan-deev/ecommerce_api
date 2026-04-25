package com.jhonatan.ecommerce_api.dto.usuario;

import com.jhonatan.ecommerce_api.enums.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequestDTO(
        @NotBlank
        String nome,
        @Email
        @NotBlank
        String email,
        @NotBlank
        String senha,
        @NotNull
        TipoUsuario tipo
) {
}
