package com.jhonatan.ecommerce_api.controller;

import com.jhonatan.ecommerce_api.dto.produto.ProdutoRequestDTO;
import com.jhonatan.ecommerce_api.dto.produto.ProdutoResponseDTO;
import com.jhonatan.ecommerce_api.model.Produto;
import com.jhonatan.ecommerce_api.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
        private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> cadastrarProduto(@RequestBody ProdutoRequestDTO produtoRequestDTO){
        ProdutoResponseDTO produto = produtoService.create(produtoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(@RequestBody @Valid ProdutoRequestDTO produtoRequestDTO,
                                                               @PathVariable Long id){
        ProdutoResponseDTO produto = produtoService.atualizarProduto(id, produtoRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(produto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> deletarProduto(@PathVariable Long id){
        produtoService.deleteProduto(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listaProdutos(){
        List<ProdutoResponseDTO> produtosDTO = produtoService.listarProdutos();
        return ResponseEntity.ok(produtosDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarProdutoPorId(Long id){
        ProdutoResponseDTO produto = produtoService.buscarProdutoPorId(id);
        return ResponseEntity.ok(produto);
    }

}
