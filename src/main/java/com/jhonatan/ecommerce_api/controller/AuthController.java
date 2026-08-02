package com.jhonatan.ecommerce_api.controller;

import com.jhonatan.ecommerce_api.dto.RefreshRequestDTO;
import com.jhonatan.ecommerce_api.dto.TokenResponseDTO;
import com.jhonatan.ecommerce_api.dto.dois_fatores.AtivarDoisFatoresDTO;
import com.jhonatan.ecommerce_api.dto.dois_fatores.DesativarDoisFatoresDTO;
import com.jhonatan.ecommerce_api.dto.dois_fatores.GerarDoisFatoresResponseDTO;
import com.jhonatan.ecommerce_api.dto.dois_fatores.VerificarDoisFatoresDTO;
import com.jhonatan.ecommerce_api.dto.login.LoginRequest;
import com.jhonatan.ecommerce_api.dto.login.LoginResponseDTO;
import com.jhonatan.ecommerce_api.dto.login.ReenviarConfirmacaoDTO;
import com.jhonatan.ecommerce_api.dto.senha.NovaSenhaRequestDTO;
import com.jhonatan.ecommerce_api.dto.senha.SolicitarRecuperacaoSenhaDTO;
import com.jhonatan.ecommerce_api.model.Usuario;
import com.jhonatan.ecommerce_api.security.JwtService;
import com.jhonatan.ecommerce_api.service.ContaConfirmacaoService;
import com.jhonatan.ecommerce_api.service.DoisFatoresService;
import com.jhonatan.ecommerce_api.service.RefreshTokenService;
import com.jhonatan.ecommerce_api.service.ResetaSenhaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final ContaConfirmacaoService contaConfirmacaoService;
    private final ResetaSenhaService resetaSenhaService;
    private final RefreshTokenService refreshTokenService;
    private final DoisFatoresService doisFatoresService;

    public AuthController(AuthenticationManager authenticationManager,
                          ContaConfirmacaoService contaConfirmacaoService, ResetaSenhaService resetaSenhaService,
                          RefreshTokenService refreshTokenService, DoisFatoresService doisFatoresService) {
        this.authenticationManager = authenticationManager;
        this.contaConfirmacaoService = contaConfirmacaoService;
        this.resetaSenhaService = resetaSenhaService;
        this.refreshTokenService = refreshTokenService;
        this.doisFatoresService = doisFatoresService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> efetuarLogin(@RequestBody @Valid LoginRequest loginRequest) {
        var autenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.senha());
        var authentication = authenticationManager.authenticate(autenticationToken);
        var usuario = (Usuario) authentication.getPrincipal();
        return ResponseEntity.ok(doisFatoresService.finalizarLogin(usuario));
    }

    @GetMapping("/confirmar-conta")
    public ResponseEntity<String> confirmarConta(@RequestParam String token) {
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

    @PostMapping("/2fa/gerar")
    public ResponseEntity<GerarDoisFatoresResponseDTO> gerarDoisFatores(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(doisFatoresService.iniciarAtivacao(usuario));
    }

    @PostMapping("/2fa/verificar")
    public ResponseEntity<LoginResponseDTO> verificarDoisFatores(@RequestBody @Valid VerificarDoisFatoresDTO dto) {
        return ResponseEntity.ok(doisFatoresService.verificarLogin(dto));
    }

    @PostMapping("/2fa/ativar")
    public ResponseEntity<List<String>> ativarDoisFatores(@AuthenticationPrincipal Usuario usuario,
                                                          @RequestBody @Valid AtivarDoisFatoresDTO dto) {
        return ResponseEntity.ok(doisFatoresService.confirmarAtivacao(usuario, dto));
    }

    @PostMapping("/2fa/desativar")
    public ResponseEntity<Void> desativarDoisFatores(@AuthenticationPrincipal Usuario usuario,
                                                     @RequestBody @Valid DesativarDoisFatoresDTO dto) {
        doisFatoresService.desativar(usuario, dto);
        return ResponseEntity.noContent().build();
    }


}