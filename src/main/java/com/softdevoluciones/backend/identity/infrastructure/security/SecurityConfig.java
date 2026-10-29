
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

                        .requestMatchers(
                                "/",
                                "/api/health",
                                "/api/auth/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs",
                                "/error"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/productos",
                                "/api/productos/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/productos",
                                "/api/productos/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                "/api/admin/devoluciones",
                                "/api/admin/devoluciones/**"
                        ).hasAnyRole("ADMIN", "OPERADOR")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/pedidos/mis-pedidos"
                        ).authenticated()
                        .requestMatchers(
                                "/api/admin/usuarios",
                                "/api/admin/usuarios/**"
                        ).hasRole("ADMIN")
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/pedidos"
                        ).hasAnyRole("ADMIN", "OPERADOR")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/pedidos/*"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/pedidos/*/estado"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                "/api/devoluciones",
                                "/api/devoluciones/**"
                        ).authenticated()

                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }
}
