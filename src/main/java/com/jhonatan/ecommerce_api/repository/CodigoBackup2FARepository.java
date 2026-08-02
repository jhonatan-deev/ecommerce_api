package com.jhonatan.ecommerce_api.repository;

import com.jhonatan.ecommerce_api.model.CodigoBackup2FA;
import com.jhonatan.ecommerce_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodigoBackup2FARepository extends JpaRepository<CodigoBackup2FA, Long> {
    List<CodigoBackup2FA> findByUsuarioAndUsadoFalse(Usuario usuario);
    void deleteByUsuario(Usuario usuario); // usado ao gerar um novo lote
}
