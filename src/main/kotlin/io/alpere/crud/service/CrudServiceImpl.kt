package io.alpere.crud.service

import com.querydsl.core.types.dsl.BooleanExpression
import io.alpere.crud.audit.AuditProvider
import io.alpere.crud.exception.EntityNotExistsException
import io.alpere.crud.model.BaseAuditEntity
import io.alpere.crud.repository.BaseRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Order
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

open class CrudServiceImpl<Entity : BaseAuditEntity>(
    protected open val repository: BaseRepository<Entity>,
    private val auditorAware: AuditProvider
) : AbstractService<Entity>(), CrudService<Entity> {

    @Transactional
    override fun findOne(id: UUID?): Entity {
        return id.run(::safeId).let {
            repository.findById(it)
                .orElseThrow { EntityNotExistsException(entityName(), it) }
        }
    }

    @Transactional
    override fun findAll(): List<Entity> {
        return repository.findAll()
    }

    @Transactional
    override fun findAll(predicate: BooleanExpression?): List<Entity> {
        return predicate?.let {
            newArrayList(repository.findAll(it))
        } ?: findAll()
    }

    @Transactional
    override fun page(predicate: BooleanExpression?, orders: List<Order>?, page: Int, size: Int): Page<Entity> {
        val pageable = orders?.let { PageRequest.of(page, size, Sort.by(orders)) }
            ?: PageRequest.of(page, size)

        return predicate?.let { repository.findAll(predicate, pageable) }
            ?: repository.findAll(pageable)
    }

    @Transactional
    override fun save(entity: Entity?): Entity {
        return entity.run(::safeEntity).let {
            repository.save(it)
        }
    }

    @Transactional
    override fun save(entities: Iterable<Entity?>?): List<Entity> {
        return entities.run(::safeEntities).let {
            repository.saveAll(it)
        }
    }

    @Transactional
    override fun softDelete(entity: Entity?): Entity {
        return entity.run(
            ::safeEntity
        ).takeIf {
            exists(it.id)
        }?.let {
            it.deletedAt = Instant.now()
            it.deletedBy = user()
            repository.save(it)
        } ?: throw EntityNotExistsException(entityName(), entity?.id)
    }

    @Transactional
    override fun delete(entity: Entity?) {
        entity.run(::safeEntity).let {
            repository.delete(it)
        }
    }

    @Transactional
    override fun softDelete(id: UUID?): Entity {
        return id.run(::findOne)
            .run(::softDelete)
    }

    @Transactional
    override fun delete(id: UUID?) {
        id.run(::safeId).let {
            repository.deleteById(it)
        }
    }

    @Transactional
    override fun softDelete(entities: Iterable<Entity?>?) {
        entities.run(::safeEntities).let { iterable ->
            iterable.forEach {
                softDelete(it)
            }
        }
    }

    @Transactional
    override fun delete(entities: Iterable<Entity?>?) {
        entities.run(::safeEntities).let {
            repository.deleteAll(it)
        }
    }

    @Transactional
    override fun softDeleteAll(ids: Iterable<UUID?>?) {
        ids.run(::safeIds).let { iterable ->
            iterable.forEach {
                softDelete(it)
            }
        }
    }

    @Transactional
    override fun deleteAll(ids: Iterable<UUID?>?) {
        ids.run(::safeIds).let {
            repository.deleteAllById(it)
        }
    }

    @Transactional
    override fun exists(id: UUID?): Boolean {
        return id.run(::safeId).let {
            repository.existsById(it)
        }
    }

    override fun findFilteredByAudit(createdBy: String?, createdFrom: Instant?, createdTo: Instant?,
                                     updatedBy: String?, updatedFrom: Instant?, updatedTo: Instant?,
                                     deletedBy: String?, deletedFrom: Instant?, deletedTo: Instant?): List<Entity> {

        return findAll(AuditFilter.createFilter(createdBy, createdFrom, createdTo,
            updatedBy, updatedFrom, updatedTo,
            deletedBy, deletedFrom, deletedTo))
    }

    private fun user(): String {
        val user = auditorAware.user()
        return user.ifEmpty { "unknown user" }
    }

}