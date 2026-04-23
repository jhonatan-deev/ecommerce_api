package com.jhonatan.ecommerce_api.service;


import com.jhonatan.ecommerce_api.dto.produto.ProdutoRequestDTO;
import com.jhonatan.ecommerce_api.dto.produto.ProdutoResponseDTO;
import com.jhonatan.ecommerce_api.exception.IdCategoriaNotFoundException;
import com.jhonatan.ecommerce_api.mapper.ProdutoMapper;
import com.jhonatan.ecommerce_api.model.Produto;
import com.jhonatan.ecommerce_api.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;
    public ProdutoService(ProdutoRepository produtoRepository, ProdutoMapper produtoMapper) {
        this.produtoRepository = produtoRepository;
        this.produtoMapper = produtoMapper;
    }

    public ProdutoResponseDTO create(ProdutoRequestDTO produtoRequestDTO) {
        Produto produto = produtoMapper.toEntity(produtoRequestDTO);
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
                .orElseThrow(()-> new IdCategoriaNotFoundException("Id de usuário não encontrado."));
         return produtoMapper.toDTO(produto);
    }

    // pegue o produto por id
    // passe o produto no updateDTO
    // depois crie o produto o salvando
     //retorne em dto
    public ProdutoResponseDTO atualizarProduto(Long id, ProdutoRequestDTO produtoRequestDTO){
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(()-> new IdCategoriaNotFoundException("Id de usuário não encontrado."));
        produtoMapper.updateEntity(produtoRequestDTO, produto);
        produto = produtoRepository.save(produto);
        return produtoMapper.toDTO(produto);

    }

    public void deleteProduto(Long id){
        produtoRepository.deleteById(id);
    }

}
