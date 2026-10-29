
package com.softdevoluciones.backend.identity.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Permitir solicitudes preflight de CORS
                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        // Rutas públicas y Swagger
                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs",
                                "/error"
                        ).permitAll()

                        // Consulta pública de productos
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/productos",
                                "/api/productos/**"
                        ).permitAll()

                        // Administración de productos
                        .requestMatchers(
                                "/api/productos",
                                "/api/productos/**"
                        ).hasRole("ADMIN")

                        // Supervisión de devoluciones
                        .requestMatchers(
                                "/api/admin/devoluciones",
                                "/api/admin/devoluciones/**"
                        ).hasAnyRole("ADMIN", "OPERADOR")

                        // Pedidos del usuario autenticado
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/pedidos/mis-pedidos"
                        ).authenticated()

                        // Administración de usuarios
                        .requestMatchers(
                                "/api/admin/usuarios",
                                "/api/admin/usuarios/**"
                        ).hasRole("ADMIN")

                        // Consulta administrativa de pedidos
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/pedidos"
                        ).hasAnyRole("ADMIN", "OPERADOR")

                        // Consulta individual de pedidos
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/pedidos/*"
                        ).authenticated()

                        // Actualización del estado de pedidos
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/pedidos/*/estado"
                        ).hasRole("ADMIN")

                        // Gestión de devoluciones
                        .requestMatchers(
                                "/api/devoluciones",
                                "/api/devoluciones/**"
                        ).authenticated()

                        // Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated()
                )

                // Filtro de autenticación JWT
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }

    // Configuración CORS para Angular y Vercel
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "https://softdevoluciones-frontend.vercel.app"
        ));

        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept"
        ));

        configuration.setExposedHeaders(List.of(
                "Authorization"
        ));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
