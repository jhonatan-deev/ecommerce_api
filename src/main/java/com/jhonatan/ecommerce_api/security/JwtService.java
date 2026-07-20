package com.jhonatan.ecommerce_api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.jhonatan.ecommerce_api.model.Usuario;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@Service
public class JwtService {

    @Value("${api.security.token.secret}")
    private String secret;
    @Value("${api.security.token.issuer}")
    private String secreteIssuer;


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


//    private Instant dataExpiracao() {
//        return LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.UTC);
//    }

    private Instant dataExpiracao() {
        return Instant.now().plus(60, ChronoUnit.MINUTES);
    }
}
