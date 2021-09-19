package io.alpere.crud.service

import com.querydsl.core.types.dsl.BooleanExpression
import io.alpere.crud.model.QBaseAuditEntity.baseAuditEntity
import java.time.Instant

class AuditFilter private constructor(
    createdBy: String?, createdFrom: Instant?, createdTo: Instant?,
    updatedBy: String?, updatedFrom: Instant?, updatedTo: Instant?,
    deletedBy: String?, deletedFrom: Instant?, deletedTo: Instant?
) {

    private var filters: BooleanExpression? = null

    companion object {
        /**
         * Method to create filter for Base entity
         *
         * @param createdBy created by user
         * @param createdFrom created date/time from
         * @param createdTo created date/time to
         * @param updatedBy updated by user
         * @param updatedFrom updated date/time from
         * @param updatedTo updated date/time to
         * @param deletedBy deleted by user
         * @param deletedFrom deleted date/time from
         * @param deletedTo deleted date/time to
         */
        fun createFilter(createdBy: String?, createdFrom: Instant?, createdTo: Instant?,
                         updatedBy: String?, updatedFrom: Instant?, updatedTo: Instant?,
                         deletedBy: String?, deletedFrom: Instant?, deletedTo: Instant?): BooleanExpression? {
            return AuditFilter(
                createdBy, createdFrom, createdTo,
                updatedBy, updatedFrom, updatedTo,
                deletedBy, deletedFrom, deletedTo
            ).filters
        }
    }

    init {
        ofCreatedBy(createdBy)
        ofCreatedAt(createdFrom, createdTo)
        ofUpdatedBy(updatedBy)
        ofUpdatedAt(updatedFrom, updatedTo)
        ofDeletedBy(deletedBy)
        ofDeletedAt(deletedFrom, deletedTo)
    }

    private val filterByCreatedBy: (String) -> BooleanExpression = {
        baseAuditEntity.createdBy.containsIgnoreCase(it)
    }

    /**
     * One or both parameters (from, to) not null
     */
    private val filterByCreatedAt: (Instant?, Instant?) -> BooleanExpression = { from, to ->
        if (from != null && to != null) {
            baseAuditEntity.createdAt.after(from).and(baseAuditEntity.createdAt.before(to))
        }
        if (from != null) {
            baseAuditEntity.createdAt.after(from)
        }
        baseAuditEntity.createdAt.before(to)

    }

    private val filterByUpdatedBy: (String) -> BooleanExpression = {
        baseAuditEntity.updatedBy.containsIgnoreCase(it)
    }

    /**
     * One or both parameters (from, to) not null
     */
    private val filterByUpdatedAt: (Instant?, Instant?) -> BooleanExpression = { from, to ->
        if (from != null && to != null) {
            baseAuditEntity.updatedAt.after(from).and(baseAuditEntity.updatedAt.before(to))
        }
        if (from != null) {
            baseAuditEntity.updatedAt.after(from)
        }
        baseAuditEntity.updatedAt.before(to)
    }

    private val filterByDeletedBy: (String) -> BooleanExpression = {
        baseAuditEntity.deletedBy.containsIgnoreCase(it)
    }

    /**
     * One or both parameters (from, to) not null
     */
    private val filterByDeletedAt: (Instant?, Instant?) -> BooleanExpression = { from, to ->
        if (from != null && to != null) {
            baseAuditEntity.deletedAt.after(from).and(baseAuditEntity.deletedAt.before(to))
        }
        if (from != null) {
            baseAuditEntity.deletedAt.after(from)
        }
        baseAuditEntity.deletedAt.before(to)
    }

    private fun ofCreatedBy(createdBy: String?) {
        addFilter(createdBy, filterByCreatedBy)
    }

    private fun ofCreatedAt(from: Instant?, to: Instant?) {
        addFilter(from, to, filterByCreatedAt)
    }

    private fun ofUpdatedBy(updatedBy: String?) {
        addFilter(updatedBy, filterByUpdatedBy)
    }

    private fun ofUpdatedAt(from: Instant?, to: Instant?) {
        addFilter(from, to, filterByUpdatedAt)
    }

    private fun ofDeletedBy(deletedBy: String?) {
        addFilter(deletedBy, filterByDeletedBy)
    }

    private fun ofDeletedAt(from: Instant?, to: Instant?) {
        addFilter(from, to, filterByDeletedAt)
    }

    private fun addFilter(by: String?, filter: (String) -> BooleanExpression?) {
        by?.let {
            filters = filters?.and(filter(it)) ?: filter(it)
        }
    }

    private fun addFilter(from: Instant?, to: Instant?, filter: (Instant?, Instant?) -> BooleanExpression?) {
        if (from != null || to != null) {
            filters = filters?.and(filter(from, to)) ?: filter(from, to)
        }
    }
}