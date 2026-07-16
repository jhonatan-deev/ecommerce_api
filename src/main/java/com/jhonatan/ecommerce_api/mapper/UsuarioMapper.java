package com.jhonatan.ecommerce_api.mapper;

import com.jhonatan.ecommerce_api.dto.usuario.UsuarioRequestDTO;
import com.jhonatan.ecommerce_api.dto.usuario.UsuarioResponseDTO;
import com.jhonatan.ecommerce_api.dto.usuario.UsuarioUpdateDTO;
import com.jhonatan.ecommerce_api.model.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    private final PasswordEncoder passwordEncoder;
    public UsuarioMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    public Usuario toEntity(UsuarioRequestDTO dto, boolean ativo) {
        Usuario usuario = new Usuario(
                dto.nome(),
                dto.email(),
                passwordEncoder.encode(dto.senha()),
                dto.tipo()
        );
        if (!ativo) {
            usuario.inativar();
        }
        return usuario;
    }

    public UsuarioResponseDTO toDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipo(),
                usuario.isAtivo()
        );
    }

    public void updateEntity(UsuarioUpdateDTO dto, Usuario entity) {

        if (dto.nome() != null) {
            entity.alterarNome(dto.nome());
        }

        if (dto.senha() != null) {
            entity.alterarSenha(passwordEncoder.encode(dto.senha()));
        }

        if (dto.tipo() != null) {
            entity.alterarTipo(dto.tipo());
        }
    }
}
