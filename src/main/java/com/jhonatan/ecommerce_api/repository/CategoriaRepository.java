package com.jhonatan.ecommerce_api.repository;

import com.jhonatan.ecommerce_api.model.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    boolean existsByNome(String nome);

    boolean existsByNomeAndIdNot(String nome, Long id);

    Page<Categoria> findByAtivoTrue(Pageable pageable);
    Optional<Categoria> findByIdAndAtivoTrue(Long id);
}
