package com.jhonatan.ecommerce_api.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private Integer estoque;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    public Produto(String nome, String descricao, BigDecimal preco, Integer estoque, Categoria categoria) {
        validarNome(nome);
        validarPreco(preco);
        validarEstoqueInicial(estoque);
        validarCategoria(categoria);

        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.categoria = categoria;
    }


    public void entradaEstoque(int quantidade) {
        validarQuantidadePositiva(quantidade, "Quantidade de entrada");
        this.estoque += quantidade;
    }

    public void saidaEstoque(int quantidade) {
        validarQuantidadePositiva(quantidade, "Quantidade de saída");
        if (this.estoque < quantidade) {
            throw new IllegalStateException(
                    String.format("Estoque insuficiente: disponível %d, solicitado %d", estoque, quantidade)
            );
        }
        this.estoque -= quantidade;
    }

    public void alterarPreco(BigDecimal novoPreco) {
        validarPreco(novoPreco);
        this.preco = novoPreco;
    }

    public void alterarDescricao(String novaDescricao) {
        this.descricao = novaDescricao;
    }

    public void alterarCategoria(Categoria novaCategoria) {
        if (novaCategoria == null) {
            throw new IllegalArgumentException("Categoria é obrigatória.");
        }
        // Se necessário, validar transições (ex: não pode trocar se houver pedidos pendentes)
        this.categoria = novaCategoria;
    }

    public void alterarNome(String nome) {
        validarNome(nome);
        this.nome = nome;
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do produto não pode ser nulo ou vazio.");
        }
    }


    private void validarPreco(BigDecimal preco) {
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero.");
        }
    }

    private void validarEstoqueInicial(Integer estoque) {
        if (estoque == null || estoque < 0) {
            throw new IllegalArgumentException("Estoque inicial não pode ser nulo ou negativo.");
        }
    }

    private void validarCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria é obrigatória.");
        }
    }

    private void validarQuantidadePositiva(int quantidade, String operacao) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException(operacao + " deve ser maior que zero.");
        }
    }
}