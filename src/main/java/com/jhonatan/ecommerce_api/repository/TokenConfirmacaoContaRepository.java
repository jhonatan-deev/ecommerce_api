package com.jhonatan.ecommerce_api.repository;

import com.jhonatan.ecommerce_api.model.TokenConfirmacaoConta;
import com.jhonatan.ecommerce_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenConfirmacaoContaRepository extends JpaRepository<TokenConfirmacaoConta, Long> {
    Optional<TokenConfirmacaoConta> findByToken(String token);

    void deleteAllByUsuario(Usuario usuario);
}
