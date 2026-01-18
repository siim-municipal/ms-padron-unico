package com.tuxoftware.ms_padron_unico.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider") // Habilita la auditoría JPA
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 1. Validaciones básicas: si no hay usuario o es anónimo
            if (authentication == null ||
                    !authentication.isAuthenticated() ||
                    authentication instanceof AnonymousAuthenticationToken) {
                return Optional.of("SISTEMA");
            }

            // 2. Si la autenticación es vía JWT (Keycloak)
            if (authentication.getPrincipal() instanceof Jwt) {
                Jwt jwt = (Jwt) authentication.getPrincipal();

                // Intentamos obtener el username legible
                String username = jwt.getClaimAsString("preferred_username");

                // Si no existe, probamos con el email
                if (username == null) {
                    username = jwt.getClaimAsString("email");
                }

                // Si encontramos alguno, lo retornamos
                if (username != null) {
                    return Optional.of(username);
                }
            }

            // 3. Fallback: Si no es JWT o no tiene claims, devolvemos el getName() (UUID)
            return Optional.ofNullable(authentication.getName());
        };
    }
}