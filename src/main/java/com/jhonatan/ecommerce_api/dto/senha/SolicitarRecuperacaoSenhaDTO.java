package com.jhonatan.ecommerce_api.dto.senha;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SolicitarRecuperacaoSenhaDTO(
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Formato de email inválido")
        String email) {
}
