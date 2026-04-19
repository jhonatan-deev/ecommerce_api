package com.jhonatan.ecommerce_api.service;

import com.jhonatan.ecommerce_api.dto.CategoriaRequestDTO;
import com.jhonatan.ecommerce_api.dto.CategoriaResponseDTO;
import com.jhonatan.ecommerce_api.exception.IdCategoriaNotFoundException;
import com.jhonatan.ecommerce_api.mapper.CategoriaMapper;
import com.jhonatan.ecommerce_api.model.Categoria;
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

    public CategoriaResponseDTO buscarCategoriaPorId(Long id){
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IdCategoriaNotFoundException("Categoria não encontrada."));
        return mapper.toDTO(categoria);
    }

    public CategoriaResponseDTO create(CategoriaRequestDTO categoriaRequestDTO){
        Categoria categoria = mapper.toEntity(categoriaRequestDTO);
        categoria = categoriaRepository.save(categoria);
        return mapper.toDTO(categoria);
    }

    public CategoriaResponseDTO update(CategoriaRequestDTO categoriaRequestDTO, Long id){
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IdCategoriaNotFoundException("Categoria não encontrada."));
        mapper.updateEntity(categoriaRequestDTO, categoria);
        categoria = categoriaRepository.save(categoria);
        return mapper.toDTO(categoria);
    }

    public void deleteCategoria(Long id){
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(()-> new IdCategoriaNotFoundException("Categoria não encontrada."));
        categoriaRepository.delete(categoria);
    }



}
