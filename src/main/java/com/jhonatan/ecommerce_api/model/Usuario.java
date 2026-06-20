package com.jhonatan.ecommerce_api.model;

import com.jhonatan.ecommerce_api.enums.TipoUsuario;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipo;

    @Column(nullable = false)
    private Boolean ativo;

    public Usuario(String nome, String email, String senha, TipoUsuario tipo) {
        validarNome(nome);
        validarEmail(email);
        validarSenha(senha);
        validarTipo(tipo);

        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
        this.ativo = true;
    }

    public void alterarNome(String novoNome) {
        validarNome(novoNome);
        this.nome = novoNome;
    }

    public void alterarSenha(String novaSenha) {
        validarSenha(novaSenha);
        this.senha = novaSenha;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void inativar() {
        this.ativo = false;
    }

    public void alterarTipo(TipoUsuario novoTipo) {
        validarTipo(novoTipo);
        this.tipo = novoTipo;
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio.");
        }
    }

    private void validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio.");
        }
    }

    private void validarSenha(String senha) {
        if (senha == null || senha.length() < 6) {
            throw new IllegalArgumentException("Senha deve ter no mínimo 6 caracteres.");
        }
    }

    private void validarTipo(TipoUsuario tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de usuário é obrigatório.");
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + tipo.name()));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    }
}