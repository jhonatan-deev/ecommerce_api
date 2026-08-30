package com.jhonatan.ecommerce_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfigurations {
    private final SecurityFilter securityFilter;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    public SecurityConfigurations(SecurityFilter securityFilter, OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) {
        this.securityFilter = securityFilter;
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable().cors(cors -> {}))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth2 -> oauth2.successHandler(oAuth2LoginSuccessHandler))
                .authorizeHttpRequests(auth -> auth
                        // 1. Público — cadastro e login
                        .requestMatchers(HttpMethod.POST, "/api/v1/usuarios").permitAll()

                        .requestMatchers(
                                "/api/v1/auth/login",
                                "/api/v1/auth/2fa/verificar",
                                "/api/v1/auth/resetar-senha",
                                "/api/v1/auth/esqueci-senha",
                                "/api/v1/auth/confirmar-conta",
                                "/api/v1/auth/reenviar-confirmacao",
                                "/api/v1/auth/refresh",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/oauth2/**",
                                "/login/oauth2/**"
                        ).permitAll()

                        // 2. Público — vitrine (catálogo)
                        .requestMatchers(HttpMethod.GET, "/api/v1/produtos", "/api/v1/produtos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/categorias", "/api/v1/categorias/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/banners", "/api/v1/banners/**").permitAll()

                        // 3. Só ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/v1/usuarios").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/usuarios/*/activate").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/usuarios/*/deactivate").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/usuarios/*/tipo").hasRole("ADMIN")

                        // 4. ADMIN ou VENDEDOR — gestão de catálogo
                        .requestMatchers(HttpMethod.POST, "/api/v1/produtos").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/produtos/**").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/produtos/*/activate").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/produtos/*/deactivate").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/categorias").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/categorias/**").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/categorias/*/activate").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/categorias/*/deactivate").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/banners").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/banners/**").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/banners/*/activate").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/banners/*/deactivate").hasAnyRole("ADMIN", "VENDEDOR")

                        // 5. ADMIN ou VENDEDOR — gestão de pedido
                        .requestMatchers(HttpMethod.GET, "/api/v1/pedidos").hasAnyRole("ADMIN", "VENDEDOR")

                        // 6. Qualquer coisa não listada acima exige login
                        .anyRequest().authenticated()
                ).addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}