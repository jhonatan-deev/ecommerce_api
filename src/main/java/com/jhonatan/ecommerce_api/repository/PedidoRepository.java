package com.jhonatan.ecommerce_api.repository;

import com.jhonatan.ecommerce_api.enums.StatusPedido;
import com.jhonatan.ecommerce_api.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Page<Pedido> findByUsuarioId(Long usuarioId, Pageable pageable);
    Page<Pedido> findByStatusPedido(StatusPedido status, Pageable pageable);
    Page<Pedido> findByUsuarioIdAndStatusPedido(Long usuarioId, StatusPedido status, Pageable pageable);
}
