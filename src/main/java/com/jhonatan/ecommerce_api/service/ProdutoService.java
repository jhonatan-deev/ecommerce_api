package com.jhonatan.ecommerce_api.service;


import com.jhonatan.ecommerce_api.dto.produto.ProdutoRequestDTO;
import com.jhonatan.ecommerce_api.dto.produto.ProdutoResponseDTO;
import com.jhonatan.ecommerce_api.dto.produto.ProdutoUpdateDTO;
import com.jhonatan.ecommerce_api.exception.IdCategoriaNotFoundException;
import com.jhonatan.ecommerce_api.exception.IdProdutoNotFoundException;
import com.jhonatan.ecommerce_api.mapper.ProdutoMapper;
import com.jhonatan.ecommerce_api.model.Categoria;
import com.jhonatan.ecommerce_api.model.Produto;
import com.jhonatan.ecommerce_api.repository.CategoriaRepository;
import com.jhonatan.ecommerce_api.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, ProdutoMapper produtoMapper, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.produtoMapper = produtoMapper;
        this.categoriaRepository = categoriaRepository;
    }
    @Transactional
    public ProdutoResponseDTO create(ProdutoRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new IdCategoriaNotFoundException("Categoria não encontrada."));
        Produto produto = produtoMapper.toEntity(dto, categoria);
        produto = produtoRepository.save(produto);
        return produtoMapper.toDTO(produto);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResponseDTO> listarProdutos(Pageable pageable) {
        return produtoRepository.findAll(pageable)
                .map(produtoMapper::toDTO);
    }
    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarProdutoPorId(Long id){
         Produto produto = produtoRepository.findById(id)
                .orElseThrow(()-> new IdProdutoNotFoundException("Produto não encontrado."));
         return produtoMapper.toDTO(produto);
    }
    @Transactional
    public ProdutoResponseDTO atualizarProduto(Long id, ProdutoUpdateDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new IdProdutoNotFoundException("Produto não encontrado."));
        Categoria categoriaProduto = null;
        if (dto.categoriaId() != null) {
            categoriaProduto = categoriaRepository.findById(dto.categoriaId())
                    .orElseThrow(() -> new IdCategoriaNotFoundException("Categoria não encontrada."));
        }
        produtoMapper.updateEntity(dto, produto, categoriaProduto);
        return produtoMapper.toDTO(produto);
    }
//    @Transactional
//    public void deleteProduto(Long id){
//        produtoRepository.deleteById(id);
//    }
//    Vamos adicionar um softDelete
}
