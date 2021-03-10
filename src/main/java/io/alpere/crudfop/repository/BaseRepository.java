package io.alpere.crudfop.repository;


import io.alpere.crudfop.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

public interface BaseRepository<Entity extends BaseEntity>
        extends JpaRepository<Entity, UUID>, QuerydslPredicateExecutor<Entity> {
}
