package com.jhonatan.ecommerce_api.controller;

import com.jhonatan.ecommerce_api.dto.LoginRequest;
import com.jhonatan.ecommerce_api.dto.TokenResponseDTO;
import com.jhonatan.ecommerce_api.model.Usuario;
import com.jhonatan.ecommerce_api.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService tokenService;

    public AuthController(AuthenticationManager authenticationManager, JwtService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }


    @PostMapping("/login")
    public ResponseEntity efetuarLogin(@RequestBody @Valid LoginRequest loginRequest) {
        var autenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.senha());
        var authentication = authenticationManager.authenticate(autenticationToken);
        var tokenJwt = tokenService.generateToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new TokenResponseDTO(tokenJwt));
    }
}
