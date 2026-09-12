package com.jhonatan.ecommerce_api.client;

import com.jhonatan.ecommerce_api.dto.pagamento.PagamentoRequestFeignDTO;
import com.jhonatan.ecommerce_api.dto.pagamento.PagamentoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "PAGAMENTOS")
public interface PagamentoClient {

    @PostMapping("/api/v1/pagamentos")
    PagamentoResponseDTO criarPagamento(
            @RequestBody PagamentoRequestFeignDTO dto
    );
}