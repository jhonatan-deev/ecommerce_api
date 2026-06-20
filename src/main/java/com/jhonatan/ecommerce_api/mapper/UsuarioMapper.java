package com.jhonatan.ecommerce_api.mapper;

import com.jhonatan.ecommerce_api.dto.usuario.UsuarioRequestDTO;
import com.jhonatan.ecommerce_api.dto.usuario.UsuarioResponseDTO;
import com.jhonatan.ecommerce_api.dto.usuario.UsuarioUpdateDTO;
import com.jhonatan.ecommerce_api.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    public Usuario toEntity(UsuarioRequestDTO dto) {
        return new Usuario(
                dto.nome(),
                dto.email(),
                dto.senha(),
                dto.tipo()
        );
    }

    public UsuarioResponseDTO toDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipo(),
                usuario.getAtivo()
        );
    }

    public void updateEntity(UsuarioUpdateDTO dto, Usuario entity) {

        if (dto.nome() != null) {
            entity.alterarNome(dto.nome());
        }

        if (dto.senha() != null) {
            entity.alterarSenha(dto.senha());
        }

        if (dto.tipo() != null) {
            entity.alterarTipo(dto.tipo());
        }
    }
}
