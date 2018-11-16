package io.alpere.common.crudfop.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import io.alpere.common.crudfop.exception.CustomException;
import io.alpere.common.crudfop.exception.NotFoundException;
import io.alpere.common.crudfop.model.BaseEntity;
import io.alpere.common.crudfop.model.OrderBy;
import io.alpere.common.crudfop.repository.BaseRepository;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static io.alpere.common.crudfop.util.Constants.ID;
import static io.alpere.common.crudfop.util.Constants.NOW;
import static io.alpere.common.crudfop.util.Logging.logError;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Getter
public abstract class AbstractServiceImpl<Entity extends BaseEntity> implements AbstractService<Entity> {
    protected final BaseRepository<Entity> repository;
    protected final Class entityClass;

    protected AbstractServiceImpl(BaseRepository<Entity> repository, Class entityClass) {
        this.repository = repository;
        this.entityClass = entityClass;
    }

    @Override
    public Entity findOne(UUID id) {
        Entity entity = repository.findById(id).orElse(null);
        if (entity == null) {
            throw new NotFoundException(String.format("Entity [%s] with ID: [%s] doesn't exist.", entityClass.getSimpleName(), id));
        }
        return entity;
    }

    @Override
    public List<Entity> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Entity> findAll(BooleanExpression predicate) {
        return newArrayList(repository.findAll(predicate));
    }

    @Override
    public Page<Entity> findAll(BooleanExpression predicate, List<OrderBy> orders, int page, int size) {
        Sort sort = createSort(orders);
        Pageable pageable = PageRequest.of(page, size, sort);

        return repository.findAll(predicate, pageable);
    }

    @Override
    @Transactional
    public Entity save(Entity entity) {
        UUID id = entity.getId();
        if (id == null) {
            // Save new entity
            return repository.save(entity);
        }
        if (!exists(id)) {
            throw new NotFoundException(String.format("Entity [%s] with ID: [%s] doesn't exist.", entityClass.getSimpleName(), id));
        }
        // Update exist entity
        return repository.save(entity);
    }

    @Override
    @Transactional
    public List<Entity> save(Iterable<Entity> entities) {
        return repository.saveAll(entities);
    }

    @Override
    @Transactional
    public boolean delete(Entity entity) {
        if (entity != null && exists(entity.getId())) {
            entity.setDeletedAt(NOW);
            // TODO replace random UUID by registered user UUID from principals
            entity.setDeletedBy(ID);
            repository.save(entity);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean delete(UUID id) {
        return delete(findOne(id));
    }

    @Override
    @Transactional
    public boolean delete(Iterable<Entity> entities) {
        entities.forEach(this::delete);
        return true;
    }

    @Override
    public boolean exists(UUID id) {
        return findOne(id) != null;
    }

    private Sort createSort(List<OrderBy> orders) {
        List<Order> order = new ArrayList<>();
        if (!isBlankCollection(orders) && isBaseEntity()) {
            orders.forEach(o -> order.add(createOrder(o.getId(), o.isDesc())));
        }
        return Sort.by(order);
    }

    private Order createOrder(String id, boolean desc) {
        switch (id) {
            case "id":
            case "createdBy":
            case "createdAt":
                return new Order(desc ? DESC : ASC, id);
            default:
                try {
                    // Just to check if classOfEntity contains field with name = id
                    entityClass.getDeclaredField(id);
                    return new Order(desc ? DESC : ASC, id);
                } catch (NoSuchFieldException e) {
                    logError("Entity [%s] doesn't have field [%s].", entityClass.getSimpleName(), id);
                    throw new CustomException(String.format("Entity [%s] doesn't have field [%s].", entityClass.getSimpleName(), id));
                }
        }
    }

    private static boolean isBlankCollection(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    private boolean isBaseEntity() {
        return BaseEntity.class.equals(entityClass.getSuperclass());
    }

}
