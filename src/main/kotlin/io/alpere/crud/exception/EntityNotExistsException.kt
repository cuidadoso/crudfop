package io.alpere.crud.exception

import java.util.*

class EntityNotExistsException(entityName: String, entityId: UUID?):
    RuntimeException("Entity $entityName with ID: $entityId doesn't exist.")