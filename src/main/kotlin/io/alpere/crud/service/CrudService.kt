package io.alpere.crud.service

import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort.Order
import java.util.*

interface CrudService<Entity> {
    /**
     * Get one entity by id
     *
     * @param id UUID of entity
     * @return one entity instance
     */
    fun findOne(id: UUID?): Entity

    /**
     * Get all entities
     *
     * @return list of entities
     */
    fun findAll(): List<Entity>

    /**
     * Get filtered entities
     *
     * @param predicate BooleanExpression
     * @return list of entities
     */
    fun findAll(predicate: BooleanExpression?): List<Entity>

    /**
     * Get filtered and ordered page of entities
     *
     * @param predicate BooleanExpression
     * @param orders    list of OrderBy
     * @param page      page number
     * @param size      page size
     * @return page of entities
     */
    fun page(predicate: BooleanExpression?, orders: List<Order>?, page: Int, size: Int): Page<Entity>

    /**
     * Create/update entity
     *
     * @param entity entity to create/update
     * @return entity
     */
    fun save(entity: Entity?): Entity

    /**
     * Create/update list of entities
     *
     * @param entities list of entities
     * @return list of entity
     */
    fun save(entities: Iterable<Entity?>?): List<Entity>

    /**
     * Delete entity to archive
     *
     * @param entity entity to delete
     * @return entity deleted to archive
     */
    fun softDelete(entity: Entity?): Entity

    /**
     * Delete entity
     *
     * @param entity entity to delete
     */
    fun delete(entity: Entity?)

    /**
     * Delete entity to archive
     *
     * @param id id of entity to delete
     * @return entity deleted to archive
     */
    fun softDelete(id: UUID?): Entity

    /**
     * Delete entity
     *
     * @param id id of entity to delete
     */
    fun delete(id: UUID?)

    /**
     * Delete entities to archive
     *
     * @param entities list of entities
     */
    fun softDelete(entities: Iterable<Entity?>?)

    /**
     * Delete entities
     *
     * @param entities list of entities
     */
    fun delete(entities: Iterable<Entity?>?)

    /**
     * Delete entities to archive
     *
     * @param ids list of entities ids
     */
    fun softDeleteAll(ids: Iterable<UUID?>?)

    /**
     * Delete entities
     *
     * @param ids list of entities ids
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