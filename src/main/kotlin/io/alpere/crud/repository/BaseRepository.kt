package io.alpere.crud.repository

import io.alpere.crud.model.BaseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import java.util.*

interface BaseRepository<Entity: BaseEntity>: JpaRepository<Entity, UUID>, QuerydslPredicateExecutor<Entity>