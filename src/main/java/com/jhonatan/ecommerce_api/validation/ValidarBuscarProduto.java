package com.jhonatan.ecommerce_api.validation;

import com.jhonatan.ecommerce_api.dto.pedido.ItemPedidoRequestDTO;
import com.jhonatan.ecommerce_api.dto.pedido.PedidoRequestDTO;
import com.jhonatan.ecommerce_api.exception.RegraDeNegocioException;
import com.jhonatan.ecommerce_api.model.Produto;

import com.jhonatan.ecommerce_api.repository.ProdutoRepository;

import java.util.List;
import java.util.Optional;

public class ValidarBuscarProduto implements ValidadorCriacaoPedido{

    private final ProdutoRepository produtoRepository;

    public ValidarBuscarProduto( ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public void validar(PedidoRequestDTO dadosPedido) {
        List<ItemPedidoRequestDTO> itens = dadosPedido.itensDoPedido();
        for (ItemPedidoRequestDTO item : itens) {
           Optional<Produto> produto = produtoRepository.findById(item.produtoId());
           if(!produto.isPresent()){
               throw new RegraDeNegocioException("Produto não encontrado com o ID: " + item.produtoId());
           }
        }
    }
}
