package com.jhonatan.ecommerce_api.validation;

import com.jhonatan.ecommerce_api.dto.pedido.ItemPedidoRequestDTO;
import com.jhonatan.ecommerce_api.dto.pedido.PedidoRequestDTO;
import com.jhonatan.ecommerce_api.exception.RegraDeNegocioException;
import com.jhonatan.ecommerce_api.repository.ItemPedidoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidarItensPedido implements ValidadorCriacaoPedido {

    private final ItemPedidoRepository itemPedidoRepository;

    public ValidarItensPedido(ItemPedidoRepository itemPedidoRepository) {
        this.itemPedidoRepository = itemPedidoRepository;
    }

    @Override
    public void validar(PedidoRequestDTO dadosPedido) {
        List<ItemPedidoRequestDTO> itens = dadosPedido.itensDoPedido();
        if(itens == null || itens.isEmpty()){
            throw new RegraDeNegocioException("A lista de itens não pode ser vazia.");
        }
        boolean contemItemNull = itens.contains(null);
        if(contemItemNull){
            throw new RegraDeNegocioException("A lista de itens não pode ter itens nulo.");
        }

        for(ItemPedidoRequestDTO item:itens){
            if(item.produtoId() == null){
                throw new RegraDeNegocioException("Todo item deve ter um produto informado.");
            }
            if(item.quantidade() == null || item.quantidade() <= 0){
                throw new RegraDeNegocioException("A quantidade do item deve ser maior que zero.");
            }

        }
    }
}
