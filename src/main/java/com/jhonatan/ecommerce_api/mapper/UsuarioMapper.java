package com.jhonatan.ecommerce_api.mapper;

import com.jhonatan.ecommerce_api.dto.usuario.UsuarioRequestDTO;
import com.jhonatan.ecommerce_api.dto.usuario.UsuarioResponseDTO;
import com.jhonatan.ecommerce_api.dto.usuario.UsuarioUpdateDTO;
import com.jhonatan.ecommerce_api.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(dto.senha());
        usuario.setTipo(dto.tipo());
        return usuario;
    }

    public UsuarioResponseDTO toDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipo()
        );
    }

    public void updateEntity(UsuarioUpdateDTO dto, Usuario entity) {

        if (dto.nome() != null) {
            entity.setNome(dto.nome());
        }

        if (dto.email() != null) {
            entity.setEmail(dto.email());
        }

        if (dto.senha() != null) {
            entity.setSenha(dto.senha());
        }

        if (dto.tipo() != null) {
            entity.setTipo(dto.tipo());
        }
    }
}
