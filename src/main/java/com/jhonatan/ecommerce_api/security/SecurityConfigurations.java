package com.jhonatan.ecommerce_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
    private final SecurityFilter securityFilter;

    public SecurityConfigurations(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. Público — cadastro e login
                        .requestMatchers("/api/v1/auth/confirmar-conta").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/usuarios").permitAll()
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers("/api/v1/auth/esqueci-senha").permitAll()
                        .requestMatchers("/api/v1/auth/resetar-senha").permitAll()

                        // 1. Público — vitrine (catálogo)
                        .requestMatchers(HttpMethod.GET, "/api/v1/produtos", "/api/v1/produtos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/categorias", "/api/v1/categorias/**").permitAll()

                        // 3. Só ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/v1/usuarios").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/usuarios/*/activate").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/usuarios/*/deactivate").hasRole("ADMIN")

                        // 4. ADMIN ou VENDEDOR — gestão de catálogo
                        .requestMatchers(HttpMethod.POST, "/api/v1/produtos").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/produtos/**").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/categorias").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/categorias/**").hasAnyRole("ADMIN", "VENDEDOR")

                        // 5. Fallback — qualquer coisa não listada acima exige login
                        .anyRequest().authenticated()
                ).addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}