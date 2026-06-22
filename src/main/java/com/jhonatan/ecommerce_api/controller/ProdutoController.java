package com.jhonatan.ecommerce_api.controller;

import com.jhonatan.ecommerce_api.dto.produto.ProdutoRequestDTO;
import com.jhonatan.ecommerce_api.dto.produto.ProdutoResponseDTO;
import com.jhonatan.ecommerce_api.dto.produto.ProdutoUpdateDTO;
import com.jhonatan.ecommerce_api.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/produtos")
public class ProdutoController {
        private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> create(@RequestBody @Valid ProdutoRequestDTO produtoRequestDTO){
        ProdutoResponseDTO produto = produtoService.create(produtoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> update(@RequestBody @Valid ProdutoUpdateDTO produtoUpdateDTO,
                                                               @PathVariable Long id){
        ProdutoResponseDTO produto = produtoService.atualizarProduto(id, produtoUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(produto);
    }


    @GetMapping
    public ResponseEntity<Page<ProdutoResponseDTO>> list(Pageable pageable){
        Page<ProdutoResponseDTO> produtosDTO = produtoService.listarProdutos(pageable);
        return ResponseEntity.ok(produtosDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> findById(@PathVariable Long id){
        ProdutoResponseDTO produto = produtoService.buscarProdutoPorId(id);
        return ResponseEntity.ok(produto);
    }

}
