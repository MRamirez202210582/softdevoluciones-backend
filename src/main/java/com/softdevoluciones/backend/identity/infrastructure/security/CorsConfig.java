
package com.softdevoluciones.backend.identity.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        // Frontend Angular local y desplegado en Vercel
        configuration.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "https://softdevoluciones-frontend.vercel.app"
        ));

        // Métodos HTTP permitidos
        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        // Encabezados permitidos
        configuration.setAllowedHeaders(List.of("*"));

        // Encabezados expuestos al frontend
        configuration.setExposedHeaders(List.of(
                "Authorization"
        ));

        // Permitir credenciales cuando sean necesarias
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}
