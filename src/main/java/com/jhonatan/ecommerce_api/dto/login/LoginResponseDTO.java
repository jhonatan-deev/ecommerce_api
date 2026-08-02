package com.jhonatan.ecommerce_api.dto.login;

public record LoginResponseDTO(
        boolean requiresTwoFactor,
        String tempToken,
        String accessToken,
        String refreshToken) {
    public static LoginResponseDTO pendente(String tempToken) {
        return new LoginResponseDTO(true, tempToken, null, null);
    }
    public static LoginResponseDTO completo(String accessToken, String refreshToken) {
        return new LoginResponseDTO(false, null, accessToken, refreshToken);
    }
}