package com.jhonatan.ecommerce_api.repository;

import com.jhonatan.ecommerce_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
}
