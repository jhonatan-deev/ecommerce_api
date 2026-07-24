package com.jhonatan.ecommerce_api.mapper;

import com.jhonatan.ecommerce_api.dto.pedido.ItemPedidoResponseDTO;
import com.jhonatan.ecommerce_api.dto.pedido.PedidoResponseDTO;
import com.jhonatan.ecommerce_api.model.ItemPedido;
import com.jhonatan.ecommerce_api.model.Pedido;
import org.springframework.stereotype.Component;

@Component
public class PedidoMapper {

    private final UsuarioMapper usuarioMapper;

    public PedidoMapper(UsuarioMapper usuarioMapper) {
        this.usuarioMapper = usuarioMapper;
    }

    public PedidoResponseDTO toDTO(Pedido pedido) {
        return new PedidoResponseDTO(
                pedido.getId(),
                usuarioMapper.toDTO(pedido.getUsuario()),
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