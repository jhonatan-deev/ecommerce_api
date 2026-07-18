package com.jhonatan.ecommerce_api.service;

import com.jhonatan.ecommerce_api.exception.TokenInvalidoException;
import com.jhonatan.ecommerce_api.model.TokenResetSenha;
import com.jhonatan.ecommerce_api.repository.PasswordResetTokenRepository;
import com.jhonatan.ecommerce_api.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ResetaSenhaService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public ResetaSenhaService(UsuarioRepository usuarioRepository, PasswordResetTokenRepository tokenRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public void solicitarRecuperacao(String email) {
        // o IfPresente é pra se o email não existir retornar a resposta da mesma forma
        // assim evita que alguém descubra quais emails estão cadastrados.
        usuarioRepository.findByEmailIgnoreCase(email).ifPresent(usuario -> {
            // Remove tokens anteriores do mesmo usuário.
            // Garante que apenas um link de reset esteja ativo por vez.
            tokenRepository.deleteAllByUsuario(usuario);
            String token = UUID.randomUUID().toString();
            tokenRepository.save(new TokenResetSenha(token, usuario));
            emailService.enviarEmailDeRecuperacao(email, token);
        });
    }

    @Transactional
    public void redefinirSenha(String token, String novaSenha) {
        TokenResetSenha resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenInvalidoException("Token inválido ou inexistente."));
        if (resetToken.isUsado()) {
            throw new TokenInvalidoException("Este link já foi utilizado. Solicite um novo.");
        }
        if (resetToken.isExpired()) {
            throw new TokenInvalidoException("Token expirado. Solicite um novo link de recuperação.");
        }

        var usuario = resetToken.getUsuario();
        usuario.alterarSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
        resetToken.marcarComoUsado();
        tokenRepository.save(resetToken);
    }
}
