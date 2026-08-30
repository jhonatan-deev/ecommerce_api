package com.jhonatan.ecommerce_api.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "banners")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(name = "imagem_url", nullable = false, length = 500)
    private String imagemUrl;

    @Column(nullable = false)
    private Integer ordem;

    @Column(nullable = false)
    private boolean ativo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    public Banner(String titulo, String imagemUrl, Integer ordem, Categoria categoria) {
        validarTitulo(titulo);
        validarImagemUrl(imagemUrl);
        validarOrdem(ordem);
        this.titulo = titulo;
        this.imagemUrl = imagemUrl;
        this.ordem = ordem;
        this.categoria = categoria;
        this.ativo = true;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void inativar() {
        this.ativo = false;
    }

    public void alterarTitulo(String novoTitulo) {
        validarTitulo(novoTitulo);
        this.titulo = novoTitulo;
    }

    public void alterarImagemUrl(String novaImagemUrl) {
        validarImagemUrl(novaImagemUrl);
        this.imagemUrl = novaImagemUrl;
    }

    public void alterarOrdem(Integer novaOrdem) {
        validarOrdem(novaOrdem);
        this.ordem = novaOrdem;
    }

    public void vincularCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void desvincularCategoria() {
        this.categoria = null;
    }

    private void validarTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("Título do banner não pode ser nulo ou vazio.");
        }
    }

    private void validarImagemUrl(String imagemUrl) {
        if (imagemUrl == null || imagemUrl.isBlank()) {
            throw new IllegalArgumentException("URL da imagem não pode ser nula ou vazia.");
        }
    }

    private void validarOrdem(Integer ordem) {
        if (ordem == null || ordem < 0) {
            throw new IllegalArgumentException("Ordem do banner deve ser um número não negativo.");
        }
    }
}