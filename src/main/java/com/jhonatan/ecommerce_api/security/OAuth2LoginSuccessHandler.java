package com.jhonatan.ecommerce_api.security;

import com.jhonatan.ecommerce_api.enums.TipoUsuario;
import com.jhonatan.ecommerce_api.model.Usuario;
import com.jhonatan.ecommerce_api.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final RestClient restClient = RestClient.create();

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public OAuth2LoginSuccessHandler(UsuarioRepository usuarioRepository, JwtService jwtService, PasswordEncoder passwordEncoder, OAuth2AuthorizedClientService authorizedClientService) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authorizedClientService = authorizedClientService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws java.io.IOException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauthUser = oauthToken.getPrincipal();

        String email = oauthUser.getAttribute("email");
        String nome = oauthUser.getAttribute("name");
        String login = oauthUser.getAttribute("login");

        if (email == null) {
            email = buscarEmailPrimarioNoGithub(oauthToken);
        }

        if (nome == null || nome.isBlank()) {
            nome = login;
        }

        final String emailFinal = email;
        final String nomeFinal = nome;

        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(emailFinal).orElseGet(() -> criarNovoUsuario(nomeFinal, emailFinal));
        String jwt = jwtService.generateToken(usuario);

        String redirectUrl = frontendUrl + "/oauth-callback?token=" + jwt;
        response.sendRedirect(redirectUrl);
    }

    private Usuario criarNovoUsuario(String nome, String email) {
        // Senha aleatória: quem loga via GitHub nunca vai usar login por
        // senha, mas o construtor de Usuario exige uma senha não vazia.
        String senhaAleatoria = passwordEncoder.encode(UUID.randomUUID().toString());
        Usuario novoUsuario = new Usuario(nome, email, senhaAleatoria, TipoUsuario.CLIENTE);
        return usuarioRepository.save(novoUsuario);
    }

    // O GitHub só devolve o e-mail no /user se ele for público. Se for
    // privado, precisamos buscar em /user/emails usando o
    // access_token que o Spring guardou para essa autorização.
    private String buscarEmailPrimarioNoGithub(OAuth2AuthenticationToken oauthToken) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());
        OAuth2AccessToken accessToken = client.getAccessToken();

        List<Map<String, Object>> emails = restClient.get().uri("https://api.github.com/user/emails").
                header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getTokenValue())
                .retrieve().body(List.class);

        return emails.stream().filter(e -> Boolean.TRUE.equals(e.get("primary")))
                .map(e -> (String) e.get("email")).findFirst()
                .orElseThrow(() -> new IllegalStateException("Não foi possível obter um e-mail do GitHub para autenticação."));
    }
}