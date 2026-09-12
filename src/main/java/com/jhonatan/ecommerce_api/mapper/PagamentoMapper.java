package com.jhonatan.ecommerce_api.mapper;

import com.jhonatan.ecommerce_api.dto.pagamento.PagamentoRequestFeignDTO;
import com.jhonatan.ecommerce_api.dto.pedido.PedidoRequestDTO;
import com.jhonatan.ecommerce_api.model.Pedido;
import org.springframework.stereotype.Component;

@Component
public class PagamentoMapper {

    public PagamentoRequestFeignDTO toFeignDTO(PedidoRequestDTO pedidoRequest, Pedido pedido) {
        return new PagamentoRequestFeignDTO(
                pedidoRequest.pagamento().valor(),
                pedidoRequest.pagamento().nome(),
                pedidoRequest.pagamento().numero(),
                pedidoRequest.pagamento().expiracao(),
                pedido.getId(),
                pedidoRequest.pagamento().formaDePagamentoId()
        );
    }
}