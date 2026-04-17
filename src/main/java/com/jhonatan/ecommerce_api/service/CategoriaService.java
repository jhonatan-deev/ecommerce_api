package com.jhonatan.ecommerce_api.service;

import com.jhonatan.ecommerce_api.dto.CategoriaResponseDTO;
import com.jhonatan.ecommerce_api.mapper.CategoriaMapper;
import com.jhonatan.ecommerce_api.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper mapper;
    public CategoriaService(CategoriaRepository categoriaRepository,
                            CategoriaMapper mapper) {
        this.categoriaRepository = categoriaRepository;
        this.mapper = mapper;
    }
    public List<CategoriaResponseDTO> listarCategorias(){
        return categoriaRepository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

}
