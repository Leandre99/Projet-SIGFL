package bj.mef.sigfl_backend.shared.domain.exception;

/**
 * Base class for business/domain errors. Map subclasses to HTTP statuses in
 * {@link bj.mef.sigfl_backend.shared.web.GlobalExceptionHandler}.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }

    protected DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
