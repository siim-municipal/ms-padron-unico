package com.tuxoftware.ms_padron_unico.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return converter;
    }

    /**
     * Convertidor capaz de leer roles de Realm y de Recurso (Cliente)
     */
    static class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

        // Nombre de tu cliente en Keycloak (según tu token: "azp": "siim-frontend")
        private static final String RESOURCE_ID = "siim-frontend";

        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            // 1. Obtener Roles del Realm (Nivel Global)
            // Estructura: realm_access: { roles: [...] }
            Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
            Collection<String> realmRoles = (realmAccess != null && realmAccess.containsKey("roles"))
                    ? (Collection<String>) realmAccess.get("roles")
                    : Collections.emptyList();

            // 2. Obtener Roles del Recurso/Cliente (Nivel Aplicación)
            // Estructura: resource_access: { "siim-frontend": { roles: [...] } }
            Map<String, Object> resourceAccess = (Map<String, Object>) jwt.getClaims().get("resource_access");
            Collection<String> resourceRoles = Collections.emptyList();

            if (resourceAccess != null && resourceAccess.containsKey(RESOURCE_ID)) {
                Map<String, Object> resource = (Map<String, Object>) resourceAccess.get(RESOURCE_ID);
                if (resource != null && resource.containsKey("roles")) {
                    resourceRoles = (Collection<String>) resource.get("roles");
                }
            }

            // 3. Combinar ambas listas y convertir a GrantedAuthority
            return Stream.concat(realmRoles.stream(), resourceRoles.stream())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
    }
}
