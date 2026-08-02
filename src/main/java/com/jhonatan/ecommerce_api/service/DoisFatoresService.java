package com.jhonatan.ecommerce_api.service;

import com.jhonatan.ecommerce_api.dto.dois_fatores.AtivarDoisFatoresDTO;
import com.jhonatan.ecommerce_api.dto.dois_fatores.DesativarDoisFatoresDTO;
import com.jhonatan.ecommerce_api.dto.dois_fatores.GerarDoisFatoresResponseDTO;
import com.jhonatan.ecommerce_api.dto.dois_fatores.VerificarDoisFatoresDTO;
import com.jhonatan.ecommerce_api.dto.login.LoginResponseDTO;
import com.jhonatan.ecommerce_api.exception.CodigoInvalidoException;
import com.jhonatan.ecommerce_api.exception.SenhaInvalidaException;
import com.jhonatan.ecommerce_api.model.RefreshToken;
import com.jhonatan.ecommerce_api.model.Usuario;
import com.jhonatan.ecommerce_api.repository.UsuarioRepository;
import com.jhonatan.ecommerce_api.security.JwtService;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
public class DoisFatoresService {

    private final SecretGenerator secretGenerator = new DefaultSecretGenerator();
    private final QrGenerator qrGenerator = new ZxingPngQrGenerator();
    private final CodeVerifier codeVerifier = new DefaultCodeVerifier(
            new DefaultCodeGenerator(), new SystemTimeProvider());

    private final UsuarioRepository usuarioRepository;
    private final JwtService tokenService;
    private final CodigoBackupService codigoBackupService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    public DoisFatoresService(UsuarioRepository usuarioRepository, JwtService tokenService,
                              CodigoBackupService codigoBackupService, RefreshTokenService refreshTokenService,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.tokenService = tokenService;
        this.codigoBackupService = codigoBackupService;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    // Chamado por POST /2fa/gerar — usuário já logado pedindo pra ativar o 2FA.
    // Ainda não salva nada no banco: o segredo viaja assinado dentro do setupToken
    // e só vira permanente se o usuário confirmar com o código certo em confirmarAtivacao().
    public GerarDoisFatoresResponseDTO iniciarAtivacao(Usuario usuario) {
        String segredo = secretGenerator.generate();
        String qrCodeBase64 = gerarQrCodeBase64(usuario.getEmail(), segredo);
        String setupToken = tokenService.generateSetupToken(usuario, segredo);
        return new GerarDoisFatoresResponseDTO(qrCodeBase64, setupToken);
    }

    // Chamado por POST /2fa/ativar — usuário confirma com o código do app autenticador.
    // Só aqui o 2FA é de fato marcado como ativo, e os códigos de backup são gerados.
    @Transactional
    public List<String> confirmarAtivacao(Usuario usuario, AtivarDoisFatoresDTO dto) {
        String segredoPendente = tokenService.getSegredoDoSetupToken(dto.setupToken(), usuario.getEmail());
        if (!codeVerifier.isValidCode(segredoPendente, dto.codigo())) {
            throw new CodigoInvalidoException("Código incorreto. Tente novamente.");
        }
        usuario.ativarDoisFatores(segredoPendente);
        usuarioRepository.save(usuario);
        return codigoBackupService.gerarCodigosParaUsuario(usuario);
    }

    // Chamado por POST /2fa/desativar — exige a senha atual como confirmação.
    public void desativar(Usuario usuario, DesativarDoisFatoresDTO dto) {
        if (!passwordEncoder.matches(dto.senhaAtual(), usuario.getPassword())) {
            throw new SenhaInvalidaException("Senha incorreta.");
        }
        usuario.desativarDoisFatores();
        usuarioRepository.save(usuario);
    }

    // Chamado por POST /login, depois que email+senha já foram validados pelo AuthenticationManager.
    // Decide se devolve os tokens completos direto, ou se pede o segundo fator primeiro.
    public LoginResponseDTO finalizarLogin(Usuario usuario) {
        if (usuario.isDoisFatoresAtivo()) {
            return LoginResponseDTO.pendente(tokenService.generateTempToken(usuario));
        }
        return gerarTokensDeAcesso(usuario);
    }

    // Chamado por POST /2fa/verificar — segunda etapa do login quando 2FA está ativo.
    // Aceita tanto o código TOTP do app quanto um código de backup.
    public LoginResponseDTO verificarLogin(VerificarDoisFatoresDTO dto) {
        Usuario usuario = tokenService.validarTempTokenERetornarUsuario(dto.tempToken());
        boolean codigoValido = codeVerifier.isValidCode(usuario.getDoisFatoresSegredo(), dto.codigo())
                || codigoBackupService.validarEConsumir(usuario, dto.codigo());
        if (!codigoValido) {
            throw new CodigoInvalidoException("Código inválido ou expirado.");
        }
        return gerarTokensDeAcesso(usuario);
    }

    private LoginResponseDTO gerarTokensDeAcesso(Usuario usuario) {
        String accessToken = tokenService.generateToken(usuario);
        RefreshToken refreshToken = refreshTokenService.gerarNovoToken(usuario);
        return LoginResponseDTO.completo(accessToken, refreshToken.getToken());
    }

    private String gerarQrCodeBase64(String email, String segredo) {
        QrData data = new QrData.Builder()
                .label(email)
                .secret(segredo)
                .issuer("EcommerceAPI")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();
        try {
            return Base64.getEncoder().encodeToString(qrGenerator.generate(data));
        } catch (QrGenerationException e) {
            throw new RuntimeException("Erro ao gerar QR Code", e);
        }
    }
}