package io.alpere.crudfop.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Order;

import java.util.List;
import java.util.UUID;

public interface CrudService<Entity> {
    /**
     * Get one entity by id
     *
     * @param id UUID of entity
     * @return one entity instance
     */
    Entity findOne(UUID id);

    /**
     * Get all entities
     *
     * @return list of entities
     */
    List<Entity> findAll();

    /**
     * Get filtered entities
     *
     * @param predicate BooleanExpression
     * @return list of entities
     */
    List<Entity> findAll(BooleanExpression predicate);

    /**
     * Get filtered and ordered page of entities
     *
     * @param predicate BooleanExpression
     * @param orders    list of OrderBy
     * @param page      page number
     * @param size      page size
     * @return page of entities
     */
    Page<Entity> page(BooleanExpression predicate, List<Order> orders, int page, int size);

    /**
     * Create/update entity
     *
     * @param entity entity to create/update
     * @return entity
     */
    Entity save(Entity entity);

    /**
     * Create/update list of entities
     *
     * @param entities list of entities
     * @return list of entity
     */
    List<Entity> save(Iterable<Entity> entities);

    /**
     * Delete entity to archive
     *
     * @param entity entity to delete
     * @return entity deleted to archive
     */
    Entity softDelete(Entity entity);

    /**
     * Delete entity
     *
     * @param entity entity to delete
     */
    void delete(Entity entity);

    /**
     * Delete entity to archive
     *
     * @param id id of entity to delete
     * @return entity deleted to archive
     */
    Entity softDelete(UUID id);

    /**
     * Delete entity
     *
     * @param id id of entity to delete
     */
    void delete(UUID id);

    /**
     * Delete entities to archive
     *
     * @param entities list of entities
     */
    void softDelete(Iterable<Entity> entities);

    /**
     * Delete entities
     *
     * @param entities list of entities
     */
    void delete(Iterable<Entity> entities);

    /**
     * Delete entities to archive
     *
     * @param ids list of entities ids
     */
    void softDeleteAll(Iterable<UUID> ids);

    /**
     * Delete entities
     *
     * @param ids list of entities ids
     */
    void deleteAll(Iterable<UUID> ids);

    /**
     * Entity with specified id is exist?
     *
     * @param id id of entity to check
     * @return true or false
     */
    boolean exists(UUID id);

}
