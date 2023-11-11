package com.example.chaincuerealestate.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.core.convert.converter.Converter
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter

@Configuration
@EnableWebSecurity
@Profile("!test")
class SecurityConfig {

    @Bean
    fun securityWebFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests { exchange -> exchange
                    .requestMatchers("/home").permitAll()
                    .requestMatchers("/houses").permitAll()
                    .requestMatchers("/house/**").permitAll()
                    .requestMatchers("/account").hasRole("user")
                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer { oauth2 -> oauth2.jwt { jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()) } }
            .exceptionHandling { handler ->
                handler.accessDeniedHandler { _, response, _ -> response.sendError(403) }
                handler.authenticationEntryPoint { _, response, _ -> response.sendError(401) }
            }
            .build()
    }

    private fun jwtAuthenticationConverter(): Converter<Jwt, out AbstractAuthenticationToken> {
        val jwtConverter = JwtAuthenticationConverter()
        jwtConverter.setJwtGrantedAuthoritiesConverter(KeycloakRealmRoleConverter())
        return jwtConverter
    }
}
