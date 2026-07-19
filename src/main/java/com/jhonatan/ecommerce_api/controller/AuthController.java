package com.jhonatan.ecommerce_api.controller;

import com.jhonatan.ecommerce_api.dto.login.LoginRequest;
import com.jhonatan.ecommerce_api.dto.login.ReenviarConfirmacaoDTO;
import com.jhonatan.ecommerce_api.dto.RefreshRequestDTO;
import com.jhonatan.ecommerce_api.dto.TokenResponseDTO;
import com.jhonatan.ecommerce_api.dto.senha.NovaSenhaRequestDTO;
import com.jhonatan.ecommerce_api.dto.senha.SolicitarRecuperacaoSenhaDTO;
import com.jhonatan.ecommerce_api.model.RefreshToken;
import com.jhonatan.ecommerce_api.model.Usuario;
import com.jhonatan.ecommerce_api.security.JwtService;
import com.jhonatan.ecommerce_api.service.ContaConfirmacaoService;
import com.jhonatan.ecommerce_api.service.RefreshTokenService;
import com.jhonatan.ecommerce_api.service.ResetaSenhaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService tokenService;
    private final ContaConfirmacaoService contaConfirmacaoService;
    private final ResetaSenhaService resetaSenhaService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authenticationManager, JwtService tokenService,
                          ContaConfirmacaoService contaConfirmacaoService, ResetaSenhaService resetaSenhaService,
                          RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.contaConfirmacaoService = contaConfirmacaoService;
        this.resetaSenhaService = resetaSenhaService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> efetuarLogin(@RequestBody @Valid LoginRequest loginRequest) {
        var autenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.senha());
        var authentication = authenticationManager.authenticate(autenticationToken);
        var usuario = (Usuario) authentication.getPrincipal();
        String accessToken = tokenService.generateToken(usuario);
        RefreshToken refreshToken = refreshTokenService.gerarNovoToken(usuario);
        return ResponseEntity.ok(new TokenResponseDTO(accessToken, refreshToken.getToken()));
    }



    @GetMapping("/confirmar-conta")
    public ResponseEntity<String> efetuarConta(@RequestParam String token) {
        contaConfirmacaoService.confirmarConta(token);
        return ResponseEntity.ok("Conta confirmada com sucesso! Você já pode fazer login.");
    }



    @PostMapping("/esqueci-senha")
    public ResponseEntity<String> solicitarResetSenha(@RequestBody @Valid SolicitarRecuperacaoSenhaDTO dto) {
        resetaSenhaService.solicitarRecuperacao(dto.email());
        return ResponseEntity.ok("Se o e-mail existir em nossa base, você receberá instruções para redefinir sua senha.");
    }

    @PostMapping("/resetar-senha")
    public ResponseEntity<String> resetarSenha(@RequestBody @Valid NovaSenhaRequestDTO dto) {
        resetaSenhaService.redefinirSenha(dto.token(), dto.novaSenha());
        return ResponseEntity.ok("Senha redefinida com sucesso! Você já pode fazer login.");
    }

    @PostMapping("/reenviar-confirmacao")
    public ResponseEntity<String> reenviarConfirmacao(@RequestBody @Valid ReenviarConfirmacaoDTO dto) {
        contaConfirmacaoService.reenviarConfirmacao(dto.email());
        return ResponseEntity.ok("Se o e-mail existir em nossa base e a conta ainda não tiver sido confirmada, você receberá um novo link.");
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> renovarToken(@RequestBody @Valid RefreshRequestDTO dto) {
        return ResponseEntity.ok(refreshTokenService.refresh(dto.refreshToken()));
    }
}