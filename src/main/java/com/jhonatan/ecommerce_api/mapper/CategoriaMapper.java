package com.jhonatan.ecommerce_api.mapper;

import com.jhonatan.ecommerce_api.dto.CategoriaRequestDTO;
import com.jhonatan.ecommerce_api.dto.CategoriaResponseDTO;
import com.jhonatan.ecommerce_api.model.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public Categoria toEntity(CategoriaRequestDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNome(dto.nome());
        categoria.setDescricao(dto.descricao());
        return categoria;
    }

    public CategoriaResponseDTO toDTO(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao()
        );
    }
}