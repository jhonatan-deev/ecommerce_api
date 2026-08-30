package com.jhonatan.ecommerce_api.controller;

import com.jhonatan.ecommerce_api.dto.banner.BannerRequestDTO;
import com.jhonatan.ecommerce_api.dto.banner.BannerResponseDTO;
import com.jhonatan.ecommerce_api.service.BannerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @GetMapping
    public List<BannerResponseDTO> listar() {
        return bannerService.listarAtivos();
    }

    @PostMapping
    public ResponseEntity<BannerResponseDTO> create(@RequestBody @Valid BannerRequestDTO request) {
        BannerResponseDTO response = bannerService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BannerResponseDTO> update(@PathVariable Long id, @RequestBody @Valid BannerRequestDTO request) {
        return ResponseEntity.ok(bannerService.atualizar(id, request));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        bannerService.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        bannerService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}