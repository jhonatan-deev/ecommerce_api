package com.jhonatan.ecommerce_api.mapper;

import com.jhonatan.ecommerce_api.dto.produto.ProdutoRequestDTO;
import com.jhonatan.ecommerce_api.dto.produto.ProdutoResponseDTO;
import com.jhonatan.ecommerce_api.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public Produto toEntity(ProdutoRequestDTO dto){
        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setPreco(dto.preco());
        produto.setQuantidadeDeEstoque(dto.estoque());
        return produto;
    }
    public ProdutoResponseDTO toDTO(Produto produto){
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getQuantidadeDeEstoque()
        );
    }

    public void updateEntity(ProdutoRequestDTO dto, Produto entity){
        entity.setNome(dto.nome());
        entity.setDescricao(dto.descricao());
        entity.setPreco(dto.preco());
        entity.setQuantidadeDeEstoque(dto.estoque());
    }
}
