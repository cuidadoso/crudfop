package io.alpere.common.crudfop.config;

import org.keycloak.KeycloakPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;

import static io.alpere.common.crudfop.util.Logging.logError;

/**
 *  Audit provider use Keycloak authentication server realization.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
public class PersistenceConfig {
    @Bean
    AuditProvider auditorProvider() {
        return new AuditProviderImpl();
    }

    public interface AuditProvider extends AuditorAware<Object> {
        String user();
        String email();
    }

    private static class AuditProviderImpl implements AuditProvider {

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

        public String user() {
            KeycloakPrincipal principal = (KeycloakPrincipal) getCurrentAuditor().orElse(null);
            if (principal == null) {
                return "unknown user";
            }
            return principal.getKeycloakSecurityContext().getToken().getPreferredUsername();
        }

        public String email() {
            KeycloakPrincipal principal = (KeycloakPrincipal) getCurrentAuditor().orElse(null);
            if (principal == null) {
                return "unknown email";
            }
            return principal.getKeycloakSecurityContext().getToken().getEmail();
        }
    }

}
