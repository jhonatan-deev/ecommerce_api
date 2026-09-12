package com.jhonatan.ecommerce_api.dto.pagamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PagamentoRequestDTO(

        @NotNull
        @Positive
        BigDecimal valor,

        @NotBlank
        @Size(max = 100)
        String nome,

        @NotBlank
        @Size(max = 19)
        String numero,

        @NotBlank
        @Size(max = 7)
        String expiracao,

        @NotNull
        Long formaDePagamentoId
) {
}