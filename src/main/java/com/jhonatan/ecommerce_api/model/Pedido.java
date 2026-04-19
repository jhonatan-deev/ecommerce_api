package com.jhonatan.ecommerce_api.model;
import com.jhonatan.ecommerce_api.enums.StatusDePagamento;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    @Column(nullable = false)
    private LocalDate data;
    @Enumerated(EnumType.STRING)
    private StatusDePagamento statusDePagamento;
    private BigDecimal valorTotalPedido;
    @OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemPedido> itensDoPedido = new ArrayList<>();

    public void adicionarItem(ItemPedido item) {
        item.setPedido(this);
        this.itensDoPedido.add(item);
    }
    public Pedido(Usuario usuario, LocalDate data, StatusDePagamento statusDePagamento, BigDecimal valorTotalPedido) {
        this.usuario = usuario;
        this.data = data;
        this.statusDePagamento = statusDePagamento;
        this.valorTotalPedido = valorTotalPedido;
    }
    public Pedido() {}

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDate getData() {
        return data;
    }

    public StatusDePagamento getStatusDePagamento() {
        return statusDePagamento;
    }

    public BigDecimal getValorTotalPedido() {
        return valorTotalPedido;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setStatusDePagamento(StatusDePagamento statusDePagamento) {
        this.statusDePagamento = statusDePagamento;
    }

    public void setValorTotalPedido(BigDecimal valorTotalPedido) {
        this.valorTotalPedido = valorTotalPedido;
    }
}
