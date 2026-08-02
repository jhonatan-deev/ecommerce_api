package com.jhonatan.ecommerce_api.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "codigos_backup_2fa")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodigoBackup2FA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "codigo_hash", nullable = false)
    private String codigoHash;

    @Column(nullable = false)
    private boolean usado;

    public CodigoBackup2FA(Usuario usuario, String codigoHash) {
        this.usuario = usuario;
        this.codigoHash = codigoHash;
        this.usado = false;
    }

    public boolean corresponde(String codigoDigitado, PasswordEncoder encoder) {
        return !usado && encoder.matches(codigoDigitado, codigoHash);
    }

    public void marcarComoUsado() {
        this.usado = true;
    }
}