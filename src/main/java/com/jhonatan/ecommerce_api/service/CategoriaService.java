package com.jhonatan.ecommerce_api.service;

import com.jhonatan.ecommerce_api.dto.categoria.CategoriaRequestDTO;
import com.jhonatan.ecommerce_api.dto.categoria.CategoriaResponseDTO;
import com.jhonatan.ecommerce_api.exception.CategoriaAlreadyExistsException;
import com.jhonatan.ecommerce_api.exception.IdCategoriaNotFoundException;
import com.jhonatan.ecommerce_api.mapper.CategoriaMapper;
import com.jhonatan.ecommerce_api.model.Categoria;
import com.jhonatan.ecommerce_api.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
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

    public List<CategoriaResponseDTO> listarCategorias() {
        return categoriaRepository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    public CategoriaResponseDTO buscarCategoriaPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IdCategoriaNotFoundException("Categoria não encontrada."));
        return mapper.toDTO(categoria);
    }

    @Transactional
    public CategoriaResponseDTO create(CategoriaRequestDTO dto) {
        if (categoriaRepository.existsByNome(dto.nome())) {
            throw new CategoriaAlreadyExistsException(
                    "Categoria já cadastrada."
            );
        }
        Categoria categoria = mapper.toEntity(dto);
        categoria = categoriaRepository.save(categoria);
        return mapper.toDTO(categoria);
    }

    @Transactional
    public CategoriaResponseDTO update(CategoriaRequestDTO dto, Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IdCategoriaNotFoundException("Categoria não encontrada."));
        if (categoriaRepository.existsByNomeAndIdNot(dto.nome(), id)) {
            throw new CategoriaAlreadyExistsException("Categoria já cadastrada.");
        }
        mapper.updateEntity(dto, categoria);
        return mapper.toDTO(categoria);
    }

//    public void deleteCategoria(Long id){
//        Categoria categoria = categoriaRepository.findById(id)
//                .orElseThrow(()-> new IdCategoriaNotFoundException("Categoria não encontrada."));
//        categoriaRepository.delete(categoria);
//    }


}
