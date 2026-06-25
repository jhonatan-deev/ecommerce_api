package com.jhonatan.ecommerce_api.validation;

import com.jhonatan.ecommerce_api.dto.pedido.ItemPedidoRequestDTO;
import com.jhonatan.ecommerce_api.dto.pedido.PedidoRequestDTO;
import com.jhonatan.ecommerce_api.exception.RegraDeNegocioException;
import com.jhonatan.ecommerce_api.model.Produto;
import com.jhonatan.ecommerce_api.repository.ProdutoRepository;

import java.util.List;
import java.util.Optional;

public class ValidarEstoque implements ValidadorCriacaoPedido{
    //validarEstoque
    //Com o produto em mãos, verificar duas coisas em sequência: se ele tem
    // estoque (quantidade disponível maior que zero), e se a quantidade pedida
    // não é maior do que o disponível. Cada caso deve lançar uma exceção
    // própria com mensagem clara.

    //Pegar os itens do pedido no dadosPedido
    //Para cada item, buscar o produto no repositório pelo produtoId
    //Verificar se o produto tem estoque disponível (quantidade maior que zero)
    //Verificar se a quantidade pedida não é maior que o estoque disponível
    //Se alguma das duas falhar, lançar exceção

    private final ProdutoRepository produtoRepository;

    public ValidarEstoque(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public void validar(PedidoRequestDTO dadosPedido) {

        List<ItemPedidoRequestDTO> itens = dadosPedido.itensDoPedido();
        for(ItemPedidoRequestDTO item : itens){
            Optional<Produto> produto = produtoRepository.findById(item.produtoId());
            Produto produtoEncontrado = produto.get();
            if(produtoEncontrado.getEstoque() <= 0){
                throw new RegraDeNegocioException("Produto sem estoque disponível.");
            }

            if(produtoEncontrado.getEstoque() < item.quantidade()){
                throw new RegraDeNegocioException("Quantidade pedida maior que o estoque disponível.");
            }
        }

    }
}
