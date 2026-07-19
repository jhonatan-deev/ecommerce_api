package com.jhonatan.ecommerce_api.service;

import com.jhonatan.ecommerce_api.dto.TokenResponseDTO;
import com.jhonatan.ecommerce_api.exception.TokenInvalidoException;
import com.jhonatan.ecommerce_api.model.RefreshToken;
import com.jhonatan.ecommerce_api.model.Usuario;
import com.jhonatan.ecommerce_api.repository.RefreshTokenRepository;
import com.jhonatan.ecommerce_api.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtService jwtService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    // Chamado no login. Sessão única: qualquer refresh token anterior
    // do usuário é apagado antes de emitir o novo.
    @Transactional
    public RefreshToken gerarNovoToken(Usuario usuario) {
        refreshTokenRepository.deleteAllByUsuario(usuario);
        return refreshTokenRepository.save(new RefreshToken(usuario));
    }

    // Chamado no /refresh. Rotação: valida o token recebido, gera um
    // access token novo E um refresh token novo, revogando o antigo.
    // Um refresh token só pode ser usado uma vez — reutilizar um já
    // trocado cai no isRevogado() e é barrado.
    @Transactional
    public TokenResponseDTO refresh(String refreshTokenValue) {
        RefreshToken tokenAtual = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new TokenInvalidoException("Refresh token inválido ou inexistente."));

        if (tokenAtual.isRevogado()) {
            throw new TokenInvalidoException("Refresh token revogado. Faça login novamente.");
        }
        if (tokenAtual.isExpired()) {
            throw new TokenInvalidoException("Refresh token expirado. Faça login novamente.");
        }

        Usuario usuario = tokenAtual.getUsuario();

        tokenAtual.revogar();
        refreshTokenRepository.save(tokenAtual);

        RefreshToken novoRefreshToken = refreshTokenRepository.save(new RefreshToken(usuario));
        String novoAccessToken = jwtService.generateToken(usuario);

        return new TokenResponseDTO(novoAccessToken, novoRefreshToken.getToken());
    }
}