package com.jhonatan.ecommerce_api.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "account_confirmation_tokens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenConfirmacaoConta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private boolean usado;

    public TokenConfirmacaoConta(Usuario usuario) {
        validarUsuario(usuario);
        this.usuario = usuario;
        this.token = UUID.randomUUID().toString();
        this.expiresAt = LocalDateTime.now().plusHours(24);
        this.usado = false;
    }

    public boolean isValido() {
        return !this.usado && LocalDateTime.now().isBefore(this.expiresAt);
    }

    public void marcarComoUsado() {
        this.usado = true;
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário é obrigatório para gerar o token.");
        }
    }
}