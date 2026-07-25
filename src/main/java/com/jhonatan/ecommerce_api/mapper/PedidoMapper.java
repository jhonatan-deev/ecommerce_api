package com.jhonatan.ecommerce_api.mapper;

import com.jhonatan.ecommerce_api.dto.pedido.ItemPedidoResponseDTO;
import com.jhonatan.ecommerce_api.dto.pedido.PedidoResponseDTO;
import com.jhonatan.ecommerce_api.dto.usuario.UsuarioResumoResponseDTO;
import com.jhonatan.ecommerce_api.model.ItemPedido;
import com.jhonatan.ecommerce_api.model.Pedido;
import org.springframework.stereotype.Component;

@Component
public class PedidoMapper {

    public PedidoResponseDTO toDTO(Pedido pedido) {
        return new PedidoResponseDTO(
                pedido.getId(),
                new UsuarioResumoResponseDTO(pedido.getUsuario().getId(), pedido.getUsuario().getNome()),
                pedido.getDataPedido().toLocalDate(),
                pedido.getStatusPedido(),
                pedido.getValorTotalPedido(),
                pedido.getItensDoPedido().stream()
                        .map(this::toItemDTO)
                        .toList()
        );
    }

    private ItemPedidoResponseDTO toItemDTO(ItemPedido item) {
        return new ItemPedidoResponseDTO(
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getPrecoTotal()
        );
    }
}