package com.sopro.project_demoday.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class ProjectDemodayCorsConfig {

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