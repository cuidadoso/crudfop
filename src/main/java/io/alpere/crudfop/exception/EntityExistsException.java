package io.alpere.crudfop.exception;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
public class EntityExistsException extends RuntimeException {

    private final String entityName;

    private final Map<String, String> causeFields;

    public EntityExistsException(String entityName, Map<String, String> causeFields) {
        super(String.format("Entity [%s] with fields constraints already exists.", entityName));
        this.entityName = entityName;
        this.causeFields = Collections.unmodifiableMap(causeFields);
    }

}
