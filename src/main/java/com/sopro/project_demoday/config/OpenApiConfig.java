package com.sopro.project_demoday.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Oficial - Projeto SOPRO 🌬️")
                        .version("1.0.0")
                        .description("Motor de tecnologia assistiva focado em automação de comunicação para indivíduos com afasia e gerenciamento da plataforma.")
                        .license(new License().name("MIT License").url("https://opensource.org/licenses/MIT")));
    }
}