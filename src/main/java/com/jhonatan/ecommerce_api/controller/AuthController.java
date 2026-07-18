package com.jhonatan.ecommerce_api.controller;

import com.jhonatan.ecommerce_api.dto.LoginRequest;
import com.jhonatan.ecommerce_api.dto.TokenResponseDTO;
import com.jhonatan.ecommerce_api.dto.senha.NovaSenhaRequestDTO;
import com.jhonatan.ecommerce_api.dto.senha.SolicitarRecuperacaoSenhaDTO;
import com.jhonatan.ecommerce_api.model.Usuario;
import com.jhonatan.ecommerce_api.security.JwtService;
import com.jhonatan.ecommerce_api.service.ContaConfirmacaoService;
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

    public AuthController(AuthenticationManager authenticationManager, JwtService tokenService, ContaConfirmacaoService contaConfirmacaoService, ResetaSenhaService resetaSenhaService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.contaConfirmacaoService = contaConfirmacaoService;
        this.resetaSenhaService = resetaSenhaService;
    }

    @PostMapping("/login")
    public ResponseEntity efetuarLogin(@RequestBody @Valid LoginRequest loginRequest) {
        var autenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.senha());
        var authentication = authenticationManager.authenticate(autenticationToken);
        String tokenJwt = tokenService.generateToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new TokenResponseDTO(tokenJwt));
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
}