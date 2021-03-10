package io.alpere.crudfop.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import io.alpere.crudfop.audit.AuditProvider;
import io.alpere.crudfop.model.BaseEntity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.transaction.annotation.Transactional;
import io.alpere.crudfop.exception.EntityNotExistsException;
import io.alpere.crudfop.repository.BaseRepository;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Getter
public class CrudServiceImpl<Entity extends BaseEntity> implements CrudService<Entity> {
    protected final BaseRepository<Entity> repository;
    private final AuditProvider auditorAware;
    protected final Class<?> entityClass;

    public CrudServiceImpl(BaseRepository<Entity> repository, AuditProvider auditorAware, Class<?> entityClass)
            throws Exception {
        if (!isBaseEntity(entityClass)) {
            throw new Exception("Error service initialization. Class not extend BaseEntity.");
        }
        this.repository = repository;
        this.auditorAware = auditorAware;
        this.entityClass = entityClass;
    }

    @Override
    @Transactional
    public Entity findOne(UUID id) {
        notNull(id);
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotExistsException(entityClass.getSimpleName(), id));
    }

    @Override
    @Transactional
    public List<Entity> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public List<Entity> findAll(BooleanExpression predicate) {
        if (predicate != null) {
            return newArrayList(repository.findAll(predicate));
        }
        return findAll();
    }

    @Override
    @Transactional
    public Page<Entity> page(BooleanExpression predicate, List<Order> orders, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        if (predicate == null) {
            return repository.findAll(pageable);
        }

        Page<Entity> tempPage = repository.findAll(predicate, pageable);
        if (tempPage.getContent().isEmpty() && tempPage.getTotalElements() > 0) {
            pageable = PageRequest.of(0, size, Sort.by(orders));
            return  repository.findAll(predicate, pageable);
        }

        return tempPage;
    }

    @Override
    @Transactional
    public Entity save(Entity entity) {
        notNull(entity);
        return repository.save(entity);
    }

    @Override
    @Transactional
    public List<Entity> save(Iterable<Entity> entities) {
        notNull(entities);
        return repository.saveAll(entities);
    }

    @Override
    @Transactional
    public Entity softDelete(Entity entity) {
        notNull(entity);
        if (exists(entity.getId())) {
            entity.setDeletedAt(Instant.now());
            entity.setDeletedBy(user());
            repository.save(entity);
        }
        return entity;
    }

    @Override
    @Transactional
    public void delete(Entity entity) {
        notNull(entity);
        repository.delete(entity);
    }

    @Override
    @Transactional
    public Entity softDelete(UUID id) {
        return softDelete(findOne(id));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        notNull(id);
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void softDelete(Iterable<Entity> entities) {
        entities.forEach(this::softDelete);
    }

    @Override
    @Transactional
    public void delete(Iterable<Entity> entities) {
        notNull(entities);
        repository.deleteAll(entities);
    }

    @Override
    @Transactional
    public void softDeleteAll(Iterable<UUID> ids) {
        notNullIds(ids);
        ids.forEach(this::softDelete);
    }

    @Override
    @Transactional
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
        String message = String.format("Id for [%s] should not be null.", entityClass.getSimpleName());
        Objects.requireNonNull(id, message);
    }

    private void notNullIds(Iterable<UUID> ids) {
        String message = String.format("Ids for [%s] should not be null.", entityClass.getSimpleName());
        Objects.requireNonNull(ids, message);
    }

    private void notNull(Entity entity) {
        String message = String.format("Entity [%s] should not be null.", entityClass.getSimpleName());
        Objects.requireNonNull(entity, message);
    }

    private void notNull(Iterable<Entity> entities) {
        String message = String.format("List of [%s] should not be null.", entityClass.getSimpleName());
        Objects.requireNonNull(entities, message);
        entities.forEach(this::notNull);
    }

    private String user() {
        String user = auditorAware.user();
        return user.isEmpty() ? "unknown user" : user;
    }

    private List<Entity> newArrayList(Iterable<Entity> elements) {
        notNull(elements);
        return StreamSupport.stream(elements.spliterator(), false)
                .collect(Collectors.toList());
    }
}
