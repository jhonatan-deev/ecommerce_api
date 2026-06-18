package com.jhonatan.ecommerce_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Bean
    public UserDetailsService dadosDoUsuarioCadastrados() {
        UserDetails usuario1 = User.builder()
                .username("jhon@gmail.com")
                .password("{noop}12345678")
                .build();
        UserDetails usuario2 = User.builder()
                .username("jhon2@gmail.com")
                .password("{noop}112345678")
                .build();
        return new InMemoryUserDetailsManager(usuario1, usuario2);
    }

    @Bean
    public SecurityFilterChain filtroDeSeguranca(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(req -> {
            req.requestMatchers("/login").permitAll();
            req.anyRequest().authenticated();
        }).formLogin(form -> form.loginPage("/login")
                .defaultSuccessUrl("/categories")
                .permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/login?logout")
                        .permitAll())
                .rememberMe(rememberMe -> rememberMe.key("lembrarDeMim")
                        .tokenValiditySeconds(36000)
                )
                .csrf(Customizer.withDefaults())
                .build();
    }
}
