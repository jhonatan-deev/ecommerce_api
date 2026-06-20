package com.jhonatan.ecommerce_api.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "itens_pedido")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    public ItemPedido(Produto produto, Integer quantidade, BigDecimal precoUnitario) {

        validarProduto(produto);
        validarQuantidade(quantidade);
        validarPreco(precoUnitario);

        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public void aumentarQuantidade(int valor) {
        validarQuantidade(valor);
        this.quantidade += valor;
    }

    public void diminuirQuantidade(int valor) {
        validarQuantidade(valor);

        int novaQuantidade = this.quantidade - valor;

        if (novaQuantidade < 1) {
            throw new IllegalArgumentException(
                    "Quantidade do item não pode ser menor que 1."
            );
        }

        this.quantidade = novaQuantidade;
    }

    public BigDecimal getPrecoTotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    private void validarProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto é obrigatório.");
        }
    }

    private void validarQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                    "Quantidade deve ser maior que zero."
            );
        }
    }

    private void validarPreco(BigDecimal preco) {
        if (preco == null || preco.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Preço inválido."
            );
        }
    }

    void associarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido é obrigatório.");
        }
        this.pedido = pedido;
    }
}