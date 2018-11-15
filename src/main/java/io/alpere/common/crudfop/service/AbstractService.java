package io.alpere.common.crudfop.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import io.alpere.common.crudfop.model.OrderBy;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface AbstractService<Entity> {
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
    Page<Entity> findAll(BooleanExpression predicate, List<OrderBy> orders, int page, int size);

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
     * Delete entity
     *
     * @param entity entity to delete
     * @return true or false
     */
    boolean delete(Entity entity);

    /**
     * Delete entity
     *
     * @param id id of entity to delete
     * @return true or false
     */
    boolean delete(UUID id);

    /**
     * Delete entities
     *
     * @param entities list of entities
     * @return true or false
     */
    boolean delete(Iterable<Entity> entities);

    /**
     * Entity with specified id is exist?
     *
     * @param id id of entity to check
     * @return true or false
     */
    boolean exists(UUID id);
}
