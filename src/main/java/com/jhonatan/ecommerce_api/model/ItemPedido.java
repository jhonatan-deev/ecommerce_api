package com.jhonatan.ecommerce_api.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "itens_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    public ItemPedido(Produto produto, Integer quantidade, BigDecimal precoUnitario) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public ItemPedido() {}

    public Long getId() { return id; }

    public Pedido getPedido() { return pedido; }

    public Produto getProduto() { return produto; }

    public Integer getQuantidade() { return quantidade; }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }

    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public void setProduto(Produto produto) { this.produto = produto; }

    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }

    public BigDecimal getPrecoTotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }
}