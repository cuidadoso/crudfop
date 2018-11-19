package io.alpere.common.crudfop.audit;

import org.springframework.data.domain.AuditorAware;

public interface AuditProvider extends AuditorAware<String> {
    String id();
    String user();
    String email();
}
