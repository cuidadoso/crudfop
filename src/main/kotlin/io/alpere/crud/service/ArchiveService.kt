package io.alpere.crud.service

import com.querydsl.core.types.dsl.BooleanExpression
import java.util.*

interface ArchiveService<ArchivedEntity> {
    /**
     * Get one entity by id
     *
     * @param id UUID of entity
     * @return one entity instance
     */
    fun findOne(id: UUID?): ArchivedEntity

    /**
     * Get all entities
     *
     * @return list of archived entities
     */
    fun findAll(): List<ArchivedEntity>?

    /**
     * Get filtered entities
     *
     * @param predicate BooleanExpression
     * @return list of entities
     */
    fun findAll(predicate: BooleanExpression?): List<ArchivedEntity>?

    /**
     * Restore entity from archive
     *
     * @return entity restored from archive
     */
    fun restore(archivedEntity: ArchivedEntity?): ArchivedEntity

    /**
     * Restore entity from archive
     *
     * @param id id of entity to restore
     * @return entity restored from archive
     */
    fun restore(id: UUID?): ArchivedEntity

    /**
     * Restore entities from archive
     *
     * @param archivedEntities list of entities
     */
    fun restore(archivedEntities: Iterable<ArchivedEntity?>?)

    /**
     * Restore entities from archive
     *
     * @param ids list of entities ids
     */
    fun restoreAll(ids: Iterable<UUID?>?)


    /**
     * Delete archived entity
     *
     * @param archivedEntity entity to delete
     */
    fun delete(archivedEntity: ArchivedEntity?)

    /**
     * Delete archived entity
     *
     * @param id id of archived entity to delete
     */
    fun delete(id: UUID?)

    /**
     * Delete archived entities
     *
     * @param archivedEntities list of entities
     */
    fun delete(archivedEntities: Iterable<ArchivedEntity?>?)

    /**
     * Delete archived entities
     *
     * @param ids list of archived entities ids
     */
    fun deleteAll(ids: Iterable<UUID?>?)

    /**
     * Entity with specified id is exists?
     *
     * @param id id of entity to check
     * @return true or false
     */
    fun exists(id: UUID?): Boolean

}