package com.jhonatan.ecommerce_api.model;
import com.jhonatan.ecommerce_api.enums.TipoUsuario;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String nome;
    @Column(nullable = false, unique = true)
    String email;
    @Column(nullable = false)
    String senha;
    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;
    Boolean ativo = true;


    public Usuario(String nome, String email, String senha, TipoUsuario tipo, Boolean ativo) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
        this.ativo = ativo;
    }

    public Usuario(){}

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
