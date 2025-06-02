package com.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Modern Banking System API")
                        .version("1.0.0")
                        .description("API for the modernized banking system")
                        .contact(new Contact()
                                .name("Banking Team")
                                .email("support@modernbanking.com")));
    }
}