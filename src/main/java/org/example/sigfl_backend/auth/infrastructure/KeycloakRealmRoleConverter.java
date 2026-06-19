package org.example.sigfl_backend.auth.infrastructure;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

/**
 * Maps a Keycloak access token to Spring Security authorities:
 * <ul>
 *   <li>standard OAuth2 scopes ({@code SCOPE_*}), and</li>
 *   <li>Keycloak realm roles from {@code realm_access.roles} as {@code ROLE_*}.</li>
 * </ul>
 */
public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES = "roles";

    private final JwtGrantedAuthoritiesConverter scopes = new JwtGrantedAuthoritiesConverter();

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Stream<GrantedAuthority> scopeAuthorities = scopes.convert(jwt).stream();
        Stream<GrantedAuthority> roleAuthorities = realmRoles(jwt).stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role));
        return Stream.concat(scopeAuthorities, roleAuthorities).toList();
    }

    private List<String> realmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaimAsMap(REALM_ACCESS);
        if (realmAccess == null || !(realmAccess.get(ROLES) instanceof List<?> roles)) {
            return List.of();
        }
        return roles.stream().map(String::valueOf).toList();
    }
}
