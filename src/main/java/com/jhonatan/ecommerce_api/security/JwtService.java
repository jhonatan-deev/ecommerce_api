package com.jhonatan.ecommerce_api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.jhonatan.ecommerce_api.exception.EmailNotFoundException;
import com.jhonatan.ecommerce_api.model.Usuario;
import com.jhonatan.ecommerce_api.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtService {

    @Value("${api.security.token.secret}")
    private String secret;
    @Value("${api.security.token.issuer}")
    private String secreteIssuer;

    private final UsuarioRepository usuarioRepository;

    public JwtService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    @PostConstruct
    public void init() {
        System.out.println("Iniciando JwtService");
        System.out.println("Secret: " + secret);
        System.out.println("Tamanho: " + secret.length());
    }

    public String generateToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(secreteIssuer)
                    .withSubject(usuario.getEmail())
                    .withExpiresAt(dataExpiracao())
                    .withClaim("id", usuario.getId())
                    .sign(algorithm);
        }catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }


    public String getSubject(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(secreteIssuer)
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException exception){
            throw new RuntimeException("Token inválido ou expirado", exception);
        }
    }


    private Instant dataExpiracao() {
        return Instant.now().plus(60, ChronoUnit.MINUTES);
    }

    // 2FA

    //Autenticação de dois fatores metodos - 2fa
    // setup token: nasce em /2fa/gerar, morre em /2fa/ativar — carrega o segredo pendente, assinado
    public String generateSetupToken(Usuario usuario, String segredo) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer(secreteIssuer)
                .withSubject(usuario.getEmail())
                .withClaim("tipo", "2fa_setup")
                .withClaim("segredo", segredo)
                .withExpiresAt(Instant.now().plus(10, ChronoUnit.MINUTES))
                .sign(algorithm);
    }

    public String getSegredoDoSetupToken(String token, String emailEsperado) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        var decoded = JWT.require(algorithm)
                .withIssuer(secreteIssuer)
                .withClaim("tipo", "2fa_setup")
                .withSubject(emailEsperado) // garante que o setupToken pertence ao mesmo usuário logado
                .build()
                .verify(token);
        return decoded.getClaim("segredo").asString();
    }

    // temp token: nasce em /login (quando 2FA está ativo), morre em /2fa/verificar
    public String generateTempToken(Usuario usuario) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer(secreteIssuer)
                .withSubject(usuario.getEmail())
                .withClaim("tipo", "2fa_pendente")
                .withExpiresAt(Instant.now().plus(5, ChronoUnit.MINUTES))
                .sign(algorithm);
    }

    public Usuario validarTempTokenERetornarUsuario(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        var decoded = JWT.require(algorithm)
                .withIssuer(secreteIssuer)
                .withClaim("tipo", "2fa_pendente")  // rejeita token de acesso normal aqui
                .build()
                .verify(token);
        return usuarioRepository.findByEmailIgnoreCase(decoded.getSubject())
                .orElseThrow(() -> new EmailNotFoundException("Usuário não encontrado"));
    }

}
