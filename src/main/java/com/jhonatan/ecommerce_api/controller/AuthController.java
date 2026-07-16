package com.jhonatan.ecommerce_api.controller;

import com.jhonatan.ecommerce_api.dto.LoginRequest;
import com.jhonatan.ecommerce_api.dto.TokenResponseDTO;
import com.jhonatan.ecommerce_api.model.Usuario;
import com.jhonatan.ecommerce_api.security.JwtService;
import com.jhonatan.ecommerce_api.service.ContaConfirmacaoService;
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

    public AuthController(AuthenticationManager authenticationManager, JwtService tokenService, ContaConfirmacaoService contaConfirmacaoService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.contaConfirmacaoService = contaConfirmacaoService;
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
}