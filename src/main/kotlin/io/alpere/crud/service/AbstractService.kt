package io.alpere.crud.service

import io.alpere.crud.model.BaseAuditEntity
import java.time.Instant
import java.util.*

abstract class AbstractService<Entity: BaseAuditEntity> {
    fun entityName(): String {
        return "Base entity"
    }

    abstract fun findFilteredByAudit(createdBy: String?, createdFrom: Instant?, createdTo: Instant?,
                            updatedBy: String?, updatedFrom: Instant?, updatedTo: Instant?,
                            deletedBy: String?, deletedFrom: Instant?, deletedTo: Instant?): List<Entity>

    protected fun newArrayList(elements: Iterable<Entity>?): List<Entity> {
        return elements?.toList() ?: emptyList()
    }

    protected fun safeId(id: UUID?): UUID {
        return checkNotNull(id) { "Id for ${entityName()} should not be null." }
    }

    protected fun safeEntity(entity: Entity?): Entity {
        entity.run {
            checkNotNull(this) { "Entity ${entityName()} should not be null." }
        }.let {
            checkNotNull(it.id) { "Id for ${entityName()} should not be null." }
        }
        return entity as Entity
    }

    protected fun safeIds(ids: Iterable<UUID?>?): Iterable<UUID> {
        return ids.run {
            checkNotNull(this) { "Ids for ${entityName()} should not be null." }
        }.map {
            safeId(it)
        }
    }

    protected fun safeEntities(entities: Iterable<Entity?>?): Iterable<Entity> {
        return entities.run {
            checkNotNull(this) { "List of ${entityName()} should not be null." }
        }.map {
            safeEntity(it)
        }
    }
}