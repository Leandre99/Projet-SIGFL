package org.example.sigfl_backend.auth.api;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes the currently authenticated principal, derived from the Keycloak
 * access token. Useful smoke test that auth is wired correctly.
 */
@RestController
@RequestMapping("/api/me")
public class MeController {

    public record CurrentUser(String subject, String username, String email, List<String> roles) {
    }

    @GetMapping
    public CurrentUser me(@AuthenticationPrincipal Jwt jwt) {
        return new CurrentUser(
                jwt.getSubject(),
                jwt.getClaimAsString("preferred_username"),
                jwt.getClaimAsString("email"),
                currentRoles());
    }

    private List<String> currentRoles() {
        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.substring("ROLE_".length()))
                .toList();
    }
}
