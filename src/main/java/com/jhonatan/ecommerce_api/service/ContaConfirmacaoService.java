package com.jhonatan.ecommerce_api.service;

import com.jhonatan.ecommerce_api.exception.TokenInvalidoException;
import com.jhonatan.ecommerce_api.model.TokenConfirmacaoConta;
import com.jhonatan.ecommerce_api.model.Usuario;
import com.jhonatan.ecommerce_api.repository.TokenConfirmacaoContaRepository;
import com.jhonatan.ecommerce_api.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ContaConfirmacaoService {

    private final UsuarioRepository usuarioRepository;
    private final TokenConfirmacaoContaRepository tokenRepository;
    private final EmailService emailService;

    public ContaConfirmacaoService(UsuarioRepository usuarioRepository,
                                   TokenConfirmacaoContaRepository tokenRepository,
                                   EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void enviarConfirmacao(Usuario usuario) {
        tokenRepository.deleteAllByUsuario(usuario);
        TokenConfirmacaoConta confirmacaoToken = tokenRepository.save(new TokenConfirmacaoConta(usuario));
        emailService.enviarEmailDeConfirmacao(usuario.getEmail(), confirmacaoToken.getToken());
    }

    @Transactional
    public void reenviarConfirmacao(String email) {
        usuarioRepository.findByEmailIgnoreCase(email).ifPresent(usuario -> {
            if (!usuario.isAtivo()) {
                enviarConfirmacao(usuario);
            }
        });
    }

    @Transactional
    public void confirmarConta(String token) {
        TokenConfirmacaoConta confirmacaoToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenInvalidoException("Token inválido ou inexistente."));

        if (confirmacaoToken.isUsado()) {
            throw new TokenInvalidoException("Esta conta já foi confirmada.");
        }
        if (!confirmacaoToken.isValido()) {
            throw new TokenInvalidoException("Token expirado. Solicite um novo cadastro ou reenvio de confirmação.");
        }

        Usuario usuario = confirmacaoToken.getUsuario();
        usuario.ativar();
        usuarioRepository.save(usuario);

        confirmacaoToken.marcarComoUsado();
        tokenRepository.save(confirmacaoToken);
    }
}
