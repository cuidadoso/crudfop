package io.alpere.crudfop.service;

import io.alpere.crudfop.exception.EntityNotExistsException;
import io.alpere.crudfop.model.BaseEntity;
import io.alpere.crudfop.repository.BaseRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class ArchiveServiceImpl<ArchivedEntity extends BaseEntity> implements ArchiveService<ArchivedEntity> {
    protected final BaseRepository<ArchivedEntity> repository;
    protected final Class<?> entityClass;

    public ArchiveServiceImpl(BaseRepository<ArchivedEntity> repository, Class<?> entityClass) throws Exception {
        if (!isBaseEntity(entityClass)) {
            throw new Exception("Error service initialization. Class not extend BaseEntity.");
        }
        this.repository = repository;
        this.entityClass = entityClass;
    }

    @Override
    public ArchivedEntity findOne(UUID id) {
        notNull(id);
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotExistsException(entityClass.getSimpleName(), id));
    }

    @Override
    public List<ArchivedEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public List<ArchivedEntity> findAll(BooleanExpression predicate) {
        if (predicate != null) {
            return newArrayList(repository.findAll(predicate));
        }
        return findAll();
    }

    @Override
    public ArchivedEntity restore(ArchivedEntity archivedEntity) {
        notNull(archivedEntity);
        if (exists(archivedEntity.getId())) {
            archivedEntity.setDeletedAt(null);
            archivedEntity.setDeletedBy(null);
            repository.save(archivedEntity);
        }
        return archivedEntity;
    }

    @Override
    public ArchivedEntity restore(UUID id) {
        return restore(findOne(id));
    }

    @Override
    public void restore(Iterable<ArchivedEntity> entities) {
        entities.forEach(this::restore);
    }

    @Override
    public void restoreAll(Iterable<UUID> ids) {
        notNullIds(ids);
        ids.forEach(this::restore);
    }

    @Override
    public void delete(ArchivedEntity archivedEntity) {
        notNull(archivedEntity);
        repository.delete(archivedEntity);
    }

    @Override
    public void delete(UUID id) {
        notNull(id);
        repository.deleteById(id);
    }

    @Override
    public void delete(Iterable<ArchivedEntity> archivedEntities) {
        notNull(archivedEntities);
        repository.deleteAll(archivedEntities);
    }

    @Override
    public void deleteAll(Iterable<UUID> ids) {
        notNullIds(ids);
        ids.forEach(this::delete);
    }

    public boolean exists(UUID id) {
        notNull(id);
        return repository.findById(id).isPresent();
    }

    private boolean isBaseEntity(Class entityClass) {
        return entityClass != BaseEntity.class && BaseEntity.class.isAssignableFrom(entityClass);
    }

    private void notNull(UUID id) {
        String message = String.format("Id for archived [%s] should not be null.", entityClass.getSimpleName());
        Objects.requireNonNull(id, message);
    }

    private void notNullIds(Iterable<UUID> ids) {
        String message = String.format("Ids for archived [%s] should not be null.", entityClass.getSimpleName());
        Objects.requireNonNull(ids, message);
    }

    private void notNull(ArchivedEntity archivedEntity) {
        String message = String.format("Archived entity [%s] should not be null.", entityClass.getSimpleName());
        Objects.requireNonNull(archivedEntity, message);
    }

    private void notNull(Iterable<ArchivedEntity> entities) {
        String message = String.format("List of [%s] should not be null.", entityClass.getSimpleName());
        Objects.requireNonNull(entities, message);
        entities.forEach(this::notNull);
    }

    private List<ArchivedEntity> newArrayList(Iterable<ArchivedEntity> elements) {
        notNull(elements);
        return StreamSupport.stream(elements.spliterator(), false)
                .collect(Collectors.toList());
    }
}
