package io.alpere.common.crudfop.audit;

import org.springframework.data.domain.AuditorAware;

public interface AuditProvider extends AuditorAware<Object> {
    String user();
    String email();
}
