package com.jhonatan.ecommerce_api.controller;

import com.jhonatan.ecommerce_api.dto.CategoriaResponseDTO;
import com.jhonatan.ecommerce_api.service.CategoriaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {
    private final CategoriaService categoriaService;
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<CategoriaResponseDTO> listar(){
        return categoriaService.listarCategorias();
    };
}
