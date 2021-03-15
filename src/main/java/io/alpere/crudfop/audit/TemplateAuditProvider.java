package io.alpere.crudfop.audit;

import java.util.Optional;

public class TemplateAuditProvider implements AuditProvider {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("Crudfop Auditor");
    }

    @Override
    public String user() {
        return "unknown user";
    }
}