package com.jhonatan.ecommerce_api.model;

import com.jhonatan.ecommerce_api.enums.StatusPedido;
import com.jhonatan.ecommerce_api.exception.PedidoStatusInvalidoException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime dataPedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido statusPedido;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotalPedido;

    @OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itensDoPedido = new ArrayList<>();

    public Pedido(Usuario usuario) {
        validarUsuario(usuario);
        this.usuario = usuario;
        this.dataPedido = LocalDateTime.now();
        this.statusPedido = StatusPedido.PENDENTE;
        this.valorTotalPedido = BigDecimal.ZERO;
    }

    public void adicionarItem(ItemPedido novoItem) {
        validarItem(novoItem);

        ItemPedido itemExistente = itensDoPedido.stream()
                .filter(item -> item.getProduto().equals(novoItem.getProduto()))
                .findFirst()
                .orElse(null);

        if (itemExistente != null) {
            itemExistente.aumentarQuantidade(novoItem.getQuantidade());
        } else {
            novoItem.associarPedido(this);
            itensDoPedido.add(novoItem);
        }
        recalcularTotal();
    }

    public void alterarStatus(StatusPedido novoStatus) {
        validarStatus(novoStatus);
        if (!this.statusPedido.podeTransitarPara(novoStatus)) {

            throw new PedidoStatusInvalidoException(String.format(
                            "Não é permitido alterar o status de %s para %s.",
                            this.statusPedido,
                            novoStatus));
        }
        this.statusPedido = novoStatus;
    }

    public void recalcularTotal() {
        this.valorTotalPedido = itensDoPedido.stream()
                .map(ItemPedido::getPrecoTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo.");
        }
    }

    private void validarItem(ItemPedido item) {
        if (item == null) {
            throw new IllegalArgumentException("Item não pode ser nulo.");
        }
    }

    private void validarStatus(StatusPedido status) {
        if (status == null) {
            throw new IllegalArgumentException("Status não pode ser nulo.");
        }
    }

}