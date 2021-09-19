package io.alpere.crud.service

import com.querydsl.core.types.dsl.BooleanExpression
import io.alpere.crud.exception.EntityNotExistsException
import io.alpere.crud.model.BaseAuditEntity
import io.alpere.crud.repository.BaseRepository
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

open class ArchiveServiceImpl<ArchivedEntity : BaseAuditEntity>(
    protected open val repository: BaseRepository<ArchivedEntity>
) : AbstractService<ArchivedEntity>(), ArchiveService<ArchivedEntity> {

    @Transactional
    override fun findOne(id: UUID?): ArchivedEntity {
        return id.run(::safeId).let {
            repository.findById(it)
                .orElseThrow { EntityNotExistsException(entityName(), it) }
        }
    }

    @Transactional
    override fun findAll(): List<ArchivedEntity> {
        return repository.findAll()
    }

    @Transactional
    override fun findAll(predicate: BooleanExpression?): List<ArchivedEntity> {
        return predicate?.let {
            newArrayList(repository.findAll(it))
        } ?: findAll()
    }

    override fun restore(archivedEntity: ArchivedEntity?): ArchivedEntity {
        return archivedEntity.run(
            ::safeEntity
        ).takeIf {
            exists(it.id)
        }?.let {
            it.deletedAt = null
            it.deletedBy = null
            repository.save(it)
        } ?: throw EntityNotExistsException(entityName(), archivedEntity?.id)
    }

    override fun restore(id: UUID?): ArchivedEntity {
        return id.run(::findOne)
            .run(::restore)
    }

    override fun restore(archivedEntities: Iterable<ArchivedEntity?>?) {
        archivedEntities.run(::safeEntities).let { iterable ->
            iterable.forEach {
                restore(it)
            }
        }
    }

    override fun restoreAll(ids: Iterable<UUID?>?) {
        ids.run(::safeIds).let { iterable ->
            iterable.forEach {
                restore(it)
            }
        }
    }

    @Transactional
    override fun delete(archivedEntity: ArchivedEntity?) {
        archivedEntity.run(::safeEntity).let {
            repository.delete(it)
        }
    }

    @Transactional
    override fun delete(id: UUID?) {
        id.run(::safeId).let {
            repository.deleteById(it)
        }
    }


    @Transactional
    override fun delete(archivedEntities: Iterable<ArchivedEntity?>?) {
        archivedEntities.run(::safeEntities).let {
            repository.deleteAll(it)
        }
    }

    @Transactional
    override fun deleteAll(ids: Iterable<UUID?>?) {
        ids.run(::safeIds).let {
            repository.deleteAllById(it)
        }
    }

    override fun exists(id: UUID?): Boolean {
        return id.run(::safeId).let {
            repository.existsById(it)
        }
    }

    @Transactional
    override fun findFilteredByAudit(createdBy: String?, createdFrom: Instant?, createdTo: Instant?,
                                     updatedBy: String?, updatedFrom: Instant?, updatedTo: Instant?,
                                     deletedBy: String?, deletedFrom: Instant?, deletedTo: Instant?): List<ArchivedEntity> {

        return findAll(AuditFilter.createFilter(createdBy, createdFrom, createdTo,
            updatedBy, updatedFrom, updatedTo,
            deletedBy, deletedFrom, deletedTo))
    }

}