package io.alpere.crudfop.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class EntityNotExistsException extends RuntimeException {
    private final String entityName;

    private final UUID entityId;

    public EntityNotExistsException(String entityName, UUID entityId) {
        super(String.format("Entity [%s] with ID: [%s] doesn't exist.", entityName, entityId));
        this.entityName = entityName;
        this.entityId = entityId;
    }
}
