package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringbootKeycloakApplication {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs.yaml").permitAll() // Autorise l'accès à Swagger
                        .anyRequest().authenticated() // Sécurise toutes les autres requêtes
                )
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt // Active la validation des JWT
                );

        return http.build();
    }
}
