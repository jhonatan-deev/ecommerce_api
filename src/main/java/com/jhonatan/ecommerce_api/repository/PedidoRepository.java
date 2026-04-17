package com.jhonatan.ecommerce_api.repository;

import com.jhonatan.ecommerce_api.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
}
