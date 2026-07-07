package com.jhonatan.ecommerce_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Bean
    public SecurityFilterChain filtroDeSeguranca(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(req -> {
                    req.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/api/v1/usuarios").permitAll();
                    req.requestMatchers(HttpMethod.GET, "/api/v1/produtos/**").permitAll();
                    req.requestMatchers(HttpMethod.GET, "/api/v1/categorias/**").permitAll();

                    req.requestMatchers(HttpMethod.POST, "/api/v1/produtos/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.PUT, "/api/v1/produtos/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.DELETE, "/api/v1/produtos/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.POST, "/api/v1/categorias/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.PUT, "/api/v1/categorias/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.DELETE, "/api/v1/categorias/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.GET, "/api/v1/usuarios/**").hasRole("ADMIN");

                    req.anyRequest().authenticated();
                })
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}