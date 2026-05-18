package com.sopro.project_demoday.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@Order(1) // Força o Spring a priorizar este arquivo de configuração antes de tudo
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Ativa o CORS e desativa o CSRF completamente para as requisições do React passarem
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                // Abre totalmente as portas para os endpoints da API do SOPRO
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/chatbot/**").permitAll()
                        .requestMatchers("/api/perfil/**").permitAll()
                        .anyRequest().permitAll() // Temporariamente libera tudo para destravar seu teste local
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // CORREÇÃO AQUI: Em vez de setAllowedOrigins("*"), usamos allowedOriginPatterns
        configuration.setAllowedOriginPatterns(List.of("*"));

        // Ou se preferir fixar a URL exata do seu Vite/React por segurança:
        // configuration.setAllowedOrigins(List.of("http://localhost:5173"));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        configuration.setAllowCredentials(true); // Se isso for true, o de cima precisava mudar

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}