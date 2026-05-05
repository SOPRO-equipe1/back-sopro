package com.sopro.project_demoday.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Libera todos os endpoints da API
                .allowedOrigins(
                        "http://localhost:5173", // URL padrão do Vite/React local
                        "https://seu-projeto-sopro.vercel.app" // URL de produção na Vercel
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos necessários para CRUD e Auth
                .allowedHeaders("*") // Permite todos os headers, importante para o JWT
                .allowCredentials(true); // Necessário se formos usar cookies ou autenticação específica
    }
}