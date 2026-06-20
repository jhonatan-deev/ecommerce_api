package com.jhonatan.ecommerce_api.service;


import com.jhonatan.ecommerce_api.dto.categoria.CategoriaRequestDTO;
import com.jhonatan.ecommerce_api.dto.produto.ProdutoRequestDTO;
import com.jhonatan.ecommerce_api.dto.produto.ProdutoResponseDTO;
import com.jhonatan.ecommerce_api.dto.produto.ProdutoUpdateDTO;
import com.jhonatan.ecommerce_api.exception.IdCategoriaNotFoundException;
import com.jhonatan.ecommerce_api.exception.IdProdutoNotFoundException;
import com.jhonatan.ecommerce_api.mapper.CategoriaMapper;
import com.jhonatan.ecommerce_api.mapper.ProdutoMapper;
import com.jhonatan.ecommerce_api.model.Categoria;
import com.jhonatan.ecommerce_api.model.Produto;
import com.jhonatan.ecommerce_api.repository.CategoriaRepository;
import com.jhonatan.ecommerce_api.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        Produto produto = produtoMapper.toEntity(dto, categoria);
        produto = produtoRepository.save(produto);
        return produtoMapper.toDTO(produto);
    }

    public List<ProdutoResponseDTO> listarProdutos(){
        return produtoRepository.findAll().stream()
                .map(produtoMapper::toDTO)
                .toList();
    }

    public ProdutoResponseDTO buscarProdutoPorId(Long id){
         Produto produto = produtoRepository.findById(id)
                .orElseThrow(()-> new IdProdutoNotFoundException("Id de usuário não encontrado."));
         return produtoMapper.toDTO(produto);
    }
    @Transactional
    public ProdutoResponseDTO atualizarProduto(Long id, ProdutoUpdateDTO dto){
        Categoria categoriaProduto = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new IdCategoriaNotFoundException("Categoria não encontrada."));
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(()-> new IdProdutoNotFoundException("Id de usuário não encontrado."));
        produtoMapper.updateEntity(dto, produto, categoriaProduto);
        produto = produtoRepository.save(produto);
        return produtoMapper.toDTO(produto);

    }
//    @Transactional
//    public void deleteProduto(Long id){
//        produtoRepository.deleteById(id);
//    }
//    Vamos adicionar um softDelete
}
