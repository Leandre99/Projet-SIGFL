package bj.mef.sigfl_backend.shared.domain.exception;

/**
 * Thrown when creating a resource that violates a uniqueness constraint. Maps to
 * HTTP 409 Conflict.
 */
public class DuplicateResourceException extends DomainException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String resource, String field, Object value) {
        super("%s already exists with %s=%s".formatted(resource, field, value));
    }
}
