package org.example.sigfl_backend.shared.domain.exception;

/**
 * Thrown when a requested aggregate/entity does not exist. Maps to HTTP 404.
 */
public class ResourceNotFoundException extends DomainException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException of(String resource, Object id) {
        return new ResourceNotFoundException("%s not found: %s".formatted(resource, id));
    }
}
