package com.jhonatan.ecommerce_api.mapper;

import com.jhonatan.ecommerce_api.dto.produto.ProdutoRequestDTO;
import com.jhonatan.ecommerce_api.dto.produto.ProdutoResponseDTO;
import com.jhonatan.ecommerce_api.dto.produto.ProdutoUpdateDTO;
import com.jhonatan.ecommerce_api.model.Categoria;
import com.jhonatan.ecommerce_api.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public Produto toEntity(ProdutoRequestDTO dto, Categoria categoria) {
        return new Produto(
                dto.nome(),
                dto.descricao(),
                dto.preco(),
                dto.estoque(),
                categoria
        );
    }

    public ProdutoResponseDTO toDTO(Produto produto){
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getEstoque()
        );
    }

    public void updateEntity(ProdutoUpdateDTO dto, Produto entity, Categoria categoria) {
        entity.alterarNome(dto.nome());
        entity.alterarDescricao(dto.descricao());
        entity.alterarPreco(dto.preco());

        if (dto.quantidadeEntrada() != null) {
            entity.entradaEstoque(dto.quantidadeEntrada());
        }

        if (dto.categoriaId() != null) {
            // A validação de categoria nula fica dentro de alterarCategoria
            entity.alterarCategoria(categoria);
        }
    }
}
