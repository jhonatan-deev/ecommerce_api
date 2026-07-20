package com.jhonatan.ecommerce_api.dto.usuario;

import com.jhonatan.ecommerce_api.enums.TipoUsuario;
import jakarta.validation.constraints.NotNull;

public record AlterarTipoDTO(
        @NotNull(message = "Tipo é obrigatório")
        TipoUsuario tipo
) {
}