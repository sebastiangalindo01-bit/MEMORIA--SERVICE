package com.gestormemoria.memoriaservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * CONFIGURACION TEMPORAL.
 * Deja los endpoints abiertos para poder probar HU-01 mientras no esta lista la
 * integracion con Keycloak. Se reemplaza al implementar RA-04 / RA-05
 * (resource server + autorizacion por rol).
 */
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(a -> a.anyRequest().permitAll());
        return http.build();
    }
}
