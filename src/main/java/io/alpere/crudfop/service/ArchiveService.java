package io.alpere.crudfop.service;

import java.util.List;
import java.util.UUID;

import com.querydsl.core.types.dsl.BooleanExpression;

public interface ArchiveService<ArchivedEntity> {
    /**
     * Get one entity by id
     *
     * @param id UUID of entity
     * @return one entity instance
     */
    ArchivedEntity findOne(UUID id);

    /**
     * Get all entities
     *
     * @return list of archived entities
     */
    List<ArchivedEntity> findAll();

    /**
     * Get filtered entities
     *
     * @param predicate BooleanExpression
     * @return list of entities
     */
    List<ArchivedEntity> findAll(BooleanExpression predicate);

    /**
     * Restore entity from archive
     *
     * @return entity restored from archive
     */
    ArchivedEntity restore(ArchivedEntity archivedEntity);

    /**
     * Restore entity from archive
     *
     * @param id id of entity to restore
     * @return entity restored from archive
     */
    ArchivedEntity restore(UUID id);

    /**
     * Restore entities from archive
     *
     * @param archivedEntities list of entities
     */
    void restore(Iterable<ArchivedEntity> archivedEntities);

    /**
     * Restore entities from archive
     *
     * @param ids list of entities ids
     */
    void restoreAll(Iterable<UUID> ids);


    /**
     * Delete archived entity
     *
     * @param archivedEntity entity to delete
     */
    void delete(ArchivedEntity archivedEntity);

    /**
     * Delete archived entity
     *
     * @param id id of archived entity to delete
     */
    void delete(UUID id);

    /**
     * Delete archived entities
     *
     * @param archivedEntities list of entities
     */
    void delete(Iterable<ArchivedEntity> archivedEntities);

    /**
     * Delete archived entities
     *
     * @param ids list of archived entities ids
     */
    void deleteAll(Iterable<UUID> ids);

}
