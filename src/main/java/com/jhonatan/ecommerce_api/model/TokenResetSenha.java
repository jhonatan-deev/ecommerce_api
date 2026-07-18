package com.jhonatan.ecommerce_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
@Getter
public class TokenResetSenha {

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

    @Column(nullable = false)
    private boolean usado;

    public TokenResetSenha() {}

    public TokenResetSenha(String token, Usuario usuario) {
        validarUsuario(usuario);
        this.token = token;
        this.usuario = usuario;
        this.expiresAt = LocalDateTime.now().plusMinutes(30);
        this.usado = false;
    }
    private void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário é obrigatório para gerar o token.");
        }
    }

    public void marcarComoUsado() {
        this.usado = true;
    }
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }
}
