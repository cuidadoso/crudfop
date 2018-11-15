package io.alpere.common.crudfop.audit;

import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static io.alpere.common.crudfop.util.Logging.logError;

public class KeycloakAuditProvider implements AuditProvider {
    @Override
    public String user() {
        KeycloakPrincipal principal = (KeycloakPrincipal) getCurrentAuditor().orElse(null);
        if (principal == null) {
            return "unknown user";
        }
        return principal.getKeycloakSecurityContext().getToken().getPreferredUsername();
    }

    @Override
    public String email() {
        KeycloakPrincipal principal = (KeycloakPrincipal) getCurrentAuditor().orElse(null);
        if (principal == null) {
            return "unknown email";
        }
        return principal.getKeycloakSecurityContext().getToken().getEmail();
    }

    @Override
    public Optional<Object> getCurrentAuditor() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.of("unauthorized");
            }

            Object principal = authentication.getPrincipal();
            if (!(principal instanceof KeycloakPrincipal)) {
                return Optional.of("wrong authorization");
            }

            return Optional.of(principal);

        } catch (Exception e) {
            logError("Не удалось разобрать токен. {}", e);
        }

        return Optional.empty();
    }
}
