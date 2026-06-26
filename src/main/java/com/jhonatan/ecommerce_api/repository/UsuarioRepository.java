package com.jhonatan.ecommerce_api.repository;

import com.jhonatan.ecommerce_api.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Page<Usuario> findByAtivoTrue(Pageable pageable);
    Optional<Usuario> findByIdAndAtivoTrue(Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
    Optional<Usuario> findByEmailIgnoreCase(String email);
    boolean existsByEmail(String email);
}
