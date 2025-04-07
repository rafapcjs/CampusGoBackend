package com.CampusGo.commons.configs.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Campus GO - API de Gestión Académica 📚",
                description = "API REST para la gestión académica integral: estudiantes, materias, matrículas, docentes y más.",
                termsOfService = "mailto:campusgo.soporte@gmail.com",
                version = "1.0.0",
                contact = @Contact(
                        name = "John Morales, Rafael Corredor y Nelson Ruiz",
                        url = "https://www.unicartagena.edu.co",
                        email = "campusgo.soporte@gmail.com"
                ),
                license = @License(
                        name = "Universidad de Cartagena - Proyecto Campus GO",
                        url = "https://www.unicartagena.edu.co"
                )
        ),
        servers = {
                @Server(
                        description = "Swagger UI - Campus GO (Local)",
                        url = "http://localhost:8080" // Local
                ),
                @Server(
                        description = "Swagger UI - Campus GO (Producción)",
                        url = "https://backendcampusgo-2.onrender.com" // Producción
                )
        }
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .name("Bearer Authentication")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}
