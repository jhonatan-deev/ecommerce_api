package com.jhonatan.ecommerce_api.service;

import com.jhonatan.ecommerce_api.dto.categoria.CategoriaRequestDTO;
import com.jhonatan.ecommerce_api.dto.categoria.CategoriaResponseDTO;
import com.jhonatan.ecommerce_api.exception.CategoriaAlreadyExistsException;
import com.jhonatan.ecommerce_api.exception.IdCategoriaNotFoundException;
import com.jhonatan.ecommerce_api.mapper.CategoriaMapper;
import com.jhonatan.ecommerce_api.model.Categoria;
import com.jhonatan.ecommerce_api.repository.CategoriaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper mapper;

    public CategoriaService(CategoriaRepository categoriaRepository,
                            CategoriaMapper mapper) {
        this.categoriaRepository = categoriaRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public Page<CategoriaResponseDTO> listarCategorias(Pageable pageable) {
        return categoriaRepository.findByAtivoTrue(pageable).map(mapper::toDTO);
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarCategoriaPorId(Long id) {
        Categoria categoria = categoriaRepository.findByIdAndAtivoTrue(id)
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

    @Transactional
    public void deactivate(Long id){
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IdCategoriaNotFoundException("Categoria não encontrada!"));
        categoria.inativar();
    }

   @Transactional
    public void activate(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IdCategoriaNotFoundException("Categoria não encontrada!"));
        categoria.ativar();
    }


}
