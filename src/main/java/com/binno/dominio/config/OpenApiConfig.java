package com.binno.dominio.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.Arrays;

@Configuration
@OpenAPIDefinition
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi allControllersApi() {
        return GroupedOpenApi.builder()
                .group("all-controllers")
                .packagesToScan("com.binno.dominio") // Adjust this package to match your controllers' package
                .build();
    }

    @Bean
    public io.swagger.v3.oas.models.OpenAPI customOpenAPI() {
        return new io.swagger.v3.oas.models.OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Binno Pecuaria Backend")
                        .description("Aplicação para gerenciar o app https://binno-agro.netlify.app/app/dashboard")
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("Apache License Version 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0"))
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("Jean")
                                .email("jeanlucafp@gmail.com")))
                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("Token JWT",
                        Arrays.asList("read", "write")))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Token JWT", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme(HttpHeaders.AUTHORIZATION)
                                .bearerFormat("JWT")));
    }
}
