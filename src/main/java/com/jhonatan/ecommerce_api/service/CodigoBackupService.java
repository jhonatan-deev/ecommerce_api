package com.jhonatan.ecommerce_api.service;

import com.jhonatan.ecommerce_api.model.CodigoBackup2FA;
import com.jhonatan.ecommerce_api.model.Usuario;
import com.jhonatan.ecommerce_api.repository.CodigoBackup2FARepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CodigoBackupService {

    private static final int QUANTIDADE_CODIGOS = 8;

    private final CodigoBackup2FARepository repository;
    private final PasswordEncoder passwordEncoder;

    public CodigoBackupService(CodigoBackup2FARepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    // Chamado quando o usuário confirma a ativação do 2FA.
    // Gera um lote novo de códigos de uso único, apagando qualquer lote anterior.
    @Transactional
    public List<String> gerarCodigosParaUsuario(Usuario usuario) {
        repository.deleteByUsuario(usuario);
        List<String> codigosEmTextoPuro = new ArrayList<>();
        for (int i = 0; i < QUANTIDADE_CODIGOS; i++) {
            String codigo = gerarCodigoAleatorio();
            repository.save(new CodigoBackup2FA(usuario, passwordEncoder.encode(codigo)));
            codigosEmTextoPuro.add(codigo);
        }
        return codigosEmTextoPuro; // única vez que existem em texto puro
    }

    // Chamado no /2fa/verificar como alternativa ao código TOTP,
    // caso o usuário tenha perdido acesso ao app autenticador.
    @Transactional
    public boolean validarEConsumir(Usuario usuario, String codigoDigitado) {
        return repository.findByUsuarioAndUsadoFalse(usuario).stream()
                .filter(codigo -> codigo.corresponde(codigoDigitado, passwordEncoder))
                .findFirst()
                .map(codigo -> {
                    codigo.marcarComoUsado();
                    repository.save(codigo);
                    return true;
                })
                .orElse(false);
    }

    private String gerarCodigoAleatorio() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}