package com.jhonatan.ecommerce_api.repository;

import com.jhonatan.ecommerce_api.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Page<Produto> findByAtivoTrue(Pageable pageable);
    Optional<Produto> findByIdAndAtivoTrue(Long id);
}
