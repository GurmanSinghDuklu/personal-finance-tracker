package com.gurman.financetracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralised error handling for the entire application.
 *
 * @RestControllerAdvice intercepts exceptions thrown by any @RestController
 * and converts them into clean, consistent JSON error responses.
 *
 * Without this class, Spring would return a generic HTML error page or a raw
 * stack trace — neither of which is useful to an API consumer.
 *
 * Each @ExceptionHandler method handles a specific exception type.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles the case where a transaction ID doesn't exist in the database.
     * Thrown by TransactionService.getTransactionById() and propagates up from
     * updateTransaction() and deleteTransaction() too.
     *
     * Returns HTTP 404 Not Found.
     */
    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(TransactionNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles validation failures triggered by @Valid on request bodies.
     * Spring throws MethodArgumentNotValidException when any @NotBlank, @Positive,
     * @NotNull etc. constraint is violated.
     *
     * Returns HTTP 400 Bad Request with a map of field → error message.
     * e.g. { "amount": "Amount must be a positive number", "description": "must not be blank" }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation failed");
        body.put("fieldErrors", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Catch-all handler for any unexpected exceptions not handled elsewhere.
     * Prevents raw stack traces from leaking in production.
     * Returns HTTP 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericError(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    /** Helper to build a consistent error response body shape. */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
