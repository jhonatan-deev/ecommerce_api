package com.jhonatan.ecommerce_api.service;

import com.jhonatan.ecommerce_api.dto.banner.BannerRequestDTO;
import com.jhonatan.ecommerce_api.dto.banner.BannerResponseDTO;
import com.jhonatan.ecommerce_api.mapper.BannerMapper;
import com.jhonatan.ecommerce_api.model.Banner;
import com.jhonatan.ecommerce_api.model.Categoria;
import com.jhonatan.ecommerce_api.repository.BannerRepository;
import com.jhonatan.ecommerce_api.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;
    private final CategoriaRepository categoriaRepository;
    private final BannerMapper bannerMapper;

    @Transactional(readOnly = true)
    public List<BannerResponseDTO> listarAtivos() {
        return bannerRepository.findByAtivoTrueOrderByOrdemAsc()
                .stream()
                .map(bannerMapper::toResponse)
                .toList();
    }

    @Transactional
    public BannerResponseDTO criar(BannerRequestDTO request) {
        Categoria categoria = buscarCategoriaOpcional(request.categoriaId());
        Banner banner = new Banner(request.titulo(), request.imagemUrl(), request.ordem(), categoria);
        return bannerMapper.toResponse(bannerRepository.save(banner));
    }

    @Transactional
    public BannerResponseDTO atualizar(Long id, BannerRequestDTO request) {
        Banner banner = buscarPorId(id);
        banner.alterarTitulo(request.titulo());
        banner.alterarImagemUrl(request.imagemUrl());
        banner.alterarOrdem(request.ordem());

        Categoria categoria = buscarCategoriaOpcional(request.categoriaId());
        if (categoria != null) {
            banner.vincularCategoria(categoria);
        } else {
            banner.desvincularCategoria();
        }

        return bannerMapper.toResponse(banner);
    }

    @Transactional
    public void ativar(Long id) {
        buscarPorId(id).ativar();
    }

    @Transactional
    public void inativar(Long id) {
        buscarPorId(id).inativar();
    }

    private Banner buscarPorId(Long id) {
        return bannerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Banner não encontrado com id: " + id));
    }

    private Categoria buscarCategoriaOpcional(Long categoriaId) {
        if (categoriaId == null) {
            return null;
        }
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com id: " + categoriaId));
    }
}