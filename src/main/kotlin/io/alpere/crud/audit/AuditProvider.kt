package io.alpere.crud.audit

import org.springframework.data.domain.AuditorAware

interface AuditProvider: AuditorAware<String> {
    fun user(): String
}