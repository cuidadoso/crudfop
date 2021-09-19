package io.alpere.crud.audit

import java.util.*

class TemplateAuditProvider: AuditProvider {
    override fun user(): String {
        return "unknown user"
    }

    override fun getCurrentAuditor(): Optional<String> {
        return Optional.of("Crud-lib Auditor")
    }
}