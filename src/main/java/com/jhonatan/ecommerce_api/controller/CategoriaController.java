package com.jhonatan.ecommerce_api.controller;

import com.jhonatan.ecommerce_api.dto.categoria.CategoriaRequestDTO;
import com.jhonatan.ecommerce_api.dto.categoria.CategoriaResponseDTO;
import com.jhonatan.ecommerce_api.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    private final CategoriaService categoriaService;
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias(){
        List<CategoriaResponseDTO> categoriasDTO = categoriaService.listarCategorias();
        return ResponseEntity.ok(categoriasDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> categoriaPorId(@PathVariable Long id){
            CategoriaResponseDTO categoria = categoriaService.buscarCategoriaPorId(id);
            return ResponseEntity.ok(categoria);
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> createCategoria(@RequestBody @Valid CategoriaRequestDTO categoriaRequestDTO){
            CategoriaResponseDTO categoria = categoriaService.create(categoriaRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> updateCategoria(@PathVariable Long id,
            @RequestBody @Valid CategoriaRequestDTO categoriaRequestDTO){
        CategoriaResponseDTO categoria = categoriaService.update(categoriaRequestDTO, id);
        return ResponseEntity.ok(categoria);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        categoriaService.deleteCategoria(id);
        return ResponseEntity.noContent().build();
    }


}
