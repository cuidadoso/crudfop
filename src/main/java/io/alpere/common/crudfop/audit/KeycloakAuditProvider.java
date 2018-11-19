package io.alpere.common.crudfop.audit;

import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static io.alpere.common.crudfop.util.Logging.logError;

public class KeycloakAuditProvider implements AuditProvider {
    @Override
    public String id() {
        KeycloakPrincipal principal = (KeycloakPrincipal) getAuthentication().orElse(null);
        if (principal == null) {
            return null;
        }
        return principal.getKeycloakSecurityContext().getToken().getSubject();
    }

    @Override
    public String user() {
        KeycloakPrincipal principal = (KeycloakPrincipal) getAuthentication().orElse(null);
        if (principal == null) {
            return "unknown user";
        }
        return principal.getKeycloakSecurityContext().getToken().getPreferredUsername();
    }

    public String email() {
        KeycloakPrincipal principal = (KeycloakPrincipal) getAuthentication().orElse(null);
        if (principal == null) {
            return "unknown email";
        }
        return principal.getKeycloakSecurityContext().getToken().getEmail();
    }


    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(id());
    }

    private Optional<Object> getAuthentication() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty();
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
