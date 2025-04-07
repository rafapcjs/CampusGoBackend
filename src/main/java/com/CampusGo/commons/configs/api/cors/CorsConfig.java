package com.CampusGo.commons.configs.api.cors;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig  implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplicar CORS para todas las rutas
                .allowedOrigins("http://localhost:8080") // Permitir solicitudes desde este origen (ajusta según tu entorno)
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Permitir estos métodos HTTP
                .allowedHeaders("*") // Permitir cualquier cabecera
                .allowCredentials(true); // Permitir enviar credenciales (cookies, cabeceras de autorización, etc.)
    }
}
