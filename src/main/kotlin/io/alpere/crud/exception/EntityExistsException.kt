package io.alpere.crud.exception

class EntityExistsException(entityName: String, causeFields: Map<String, String>) :
    RuntimeException("Entity $entityName with fields constraints already exists. Fields: $causeFields")