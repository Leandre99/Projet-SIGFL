package org.example.sigfl_backend.shared.web;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.example.sigfl_backend.shared.domain.exception.DomainException;
import org.example.sigfl_backend.shared.domain.exception.DuplicateResourceException;
import org.example.sigfl_backend.shared.domain.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Translates exceptions into the normalised {@link ApiError} JSON payload so
 * every module returns consistent error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), req);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiError> handleDuplicate(DuplicateResourceException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), req);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiError> handleDomain(DomainException ex, HttpServletRequest req) {
        return build(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage(), req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, List<String>> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        fe -> fe.getField(),
                        Collectors.mapping(fe -> fe.getDefaultMessage(), Collectors.toList())));
        ApiError body = ApiError.of(HttpStatus.BAD_REQUEST.value(), "Validation Failed",
                "Request validation failed", req.getRequestURI(), fieldErrors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception ex, HttpServletRequest req) {
        log.error("Unhandled exception on {} {}", req.getMethod(), req.getRequestURI(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", req);
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String message, HttpServletRequest req) {
        ApiError body = ApiError.of(status.value(), status.getReasonPhrase(), message, req.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }
}
