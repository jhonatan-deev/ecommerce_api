package com.jhonatan.ecommerce_api.service;

import com.jhonatan.ecommerce_api.dto.usuario.UsuarioRequestDTO;
import com.jhonatan.ecommerce_api.dto.usuario.UsuarioResponseDTO;
import com.jhonatan.ecommerce_api.dto.usuario.UsuarioUpdateDTO;
import com.jhonatan.ecommerce_api.exception.EmailAlreadyExistsException;
import com.jhonatan.ecommerce_api.exception.UsuarioNotFoundException;
import com.jhonatan.ecommerce_api.mapper.UsuarioMapper;
import com.jhonatan.ecommerce_api.model.Usuario;
import com.jhonatan.ecommerce_api.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponseDTO create(UsuarioRequestDTO dto) {
        if(usuarioRepository.existsByEmail(dto.email())){
            throw new EmailAlreadyExistsException("Email já está cadastrado!");
        }
        Usuario usuario = usuarioMapper.toEntity(dto, false);
        usuario.alterarSenha(passwordEncoder.encode(dto.senha()));
        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(usuario);
    }

    @Transactional
    public void deactivate(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado."));
        usuario.inativar();
    }

    @Transactional
    public void activate(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado."));
        usuario.ativar();
    }

    @Transactional
    public UsuarioResponseDTO updateUser(Long id, UsuarioUpdateDTO dto) {
        Usuario usuario = usuarioRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado."));

        if(usuarioRepository.existsByEmailAndIdNot(dto.email(), id)){
            throw new EmailAlreadyExistsException("Email já está cadastrado!");
        }

        usuarioMapper.updateEntity(dto, usuario);
        return usuarioMapper.toDTO(usuario);
    }

    public UsuarioResponseDTO searchUserById(Long id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado!"));
        return usuarioMapper.toDTO(usuario);
    }

    public Page<UsuarioResponseDTO> listUsers(Pageable pageable) {
        return usuarioRepository.findByAtivoTrue(pageable).map(usuarioMapper::toDTO);
    }


}
