package com.jhonatan.ecommerce_api.repository;

import com.jhonatan.ecommerce_api.model.RefreshToken;
import com.jhonatan.ecommerce_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    // Sessão única: apaga qualquer refresh token anterior do usuário
    // antes de emitir um novo (no login ou no refresh).
    void deleteAllByUsuario(Usuario usuario);
}
