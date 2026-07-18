package com.jhonatan.ecommerce_api.dto.usuario;

import com.jhonatan.ecommerce_api.enums.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
        @NotBlank
        String nome,
        @Email
        @NotBlank
        String email,
        @NotBlank
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String senha,
        @NotNull
        TipoUsuario tipo
) {
}
