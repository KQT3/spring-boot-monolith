package com.example.chaincuerealestate.configs

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import java.util.stream.Collectors


class KeycloakRealmRoleConverter : Converter<Jwt, Collection<GrantedAuthority>> {
    override fun convert(jwt: Jwt): Collection<GrantedAuthority> {
        val realmAccess = jwt.claims["realm_access"] as Map<String, Any>
        return (realmAccess["roles"] as List<String>).stream()
            .map { roleName -> SimpleGrantedAuthority("ROLE_$roleName") }
            .collect(Collectors.toList())
    }
}
