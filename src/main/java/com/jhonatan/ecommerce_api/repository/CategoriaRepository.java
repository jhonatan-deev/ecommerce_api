package com.jhonatan.ecommerce_api.repository;

import com.jhonatan.ecommerce_api.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
