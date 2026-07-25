package com.jhonatan.ecommerce_api.controller;

import com.jhonatan.ecommerce_api.dto.categoria.CategoriaRequestDTO;
import com.jhonatan.ecommerce_api.dto.categoria.CategoriaResponseDTO;
import com.jhonatan.ecommerce_api.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }


    @GetMapping
    public ResponseEntity<Page<CategoriaResponseDTO>> listCategories(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable){
        return ResponseEntity.ok(categoriaService.listarCategorias(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> findCategoryById(@PathVariable Long id){
            CategoriaResponseDTO categoria = categoriaService.buscarCategoriaPorId(id);
            return ResponseEntity.ok(categoria);
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> createCategory(@RequestBody @Valid CategoriaRequestDTO dto){
            CategoriaResponseDTO categoria = categoriaService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> updateCategory(@PathVariable Long id,
            @RequestBody @Valid CategoriaRequestDTO dto){
        CategoriaResponseDTO categoria = categoriaService.update(dto, id);
        return ResponseEntity.ok(categoria);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        categoriaService.activate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        categoriaService.deactivate(id);
        return ResponseEntity.noContent().build();
    }


}
