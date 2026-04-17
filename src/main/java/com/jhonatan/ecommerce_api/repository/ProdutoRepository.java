package com.jhonatan.ecommerce_api.repository;

import com.jhonatan.ecommerce_api.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
