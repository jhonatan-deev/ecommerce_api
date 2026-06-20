package com.jhonatan.ecommerce_api.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "categorias")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    @Column(nullable = false, length = 255)
    private String descricao;

    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    private List<Produto> produtos = new ArrayList<>();


    public Categoria(String nome, String descricao) {
        validarNome(nome);
        this.nome = nome;
        this.descricao = descricao;
    }

    public void alterarNome(String novoNome) {
        validarNome(novoNome);
        this.nome = novoNome;
    }

    public void alterarDescricao(String novaDescricao) {
        this.descricao = novaDescricao;
    }

    public List<Produto> getProdutos() {
        return Collections.unmodifiableList(produtos);
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da categoria não pode ser nulo ou vazio.");
        }
    }
}