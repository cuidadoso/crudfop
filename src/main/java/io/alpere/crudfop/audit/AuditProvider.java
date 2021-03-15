package io.alpere.crudfop.audit;

import org.springframework.data.domain.AuditorAware;

public interface AuditProvider extends AuditorAware<String> {
    String user();
}
